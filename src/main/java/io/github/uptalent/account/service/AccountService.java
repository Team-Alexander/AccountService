package io.github.uptalent.account.service;

import io.github.uptalent.account.client.AuthClient;
import io.github.uptalent.account.exception.NoSuchRoleException;
import io.github.uptalent.account.exception.TokenNotFoundException;
import io.github.uptalent.account.exception.UserNotFoundException;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.hash.DeletedAccount;
import io.github.uptalent.account.model.hash.TokenEmail;
import io.github.uptalent.account.model.request.AccountUpdate;
import io.github.uptalent.account.model.request.AuthLogin;
import io.github.uptalent.account.model.request.AuthRegister;
import io.github.uptalent.account.model.request.ChangePassword;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.model.response.AuthResponse;
import io.github.uptalent.account.repository.AccountRepository;
import io.github.uptalent.account.repository.TokenEmailRepository;
import io.github.uptalent.account.service.visitor.AccountRegisterVisitor;
import io.github.uptalent.account.service.visitor.AccountUpdateVisitor;
import io.github.uptalent.starter.model.common.Author;
import io.github.uptalent.starter.model.common.EmailMessageDetailInfo;
import io.github.uptalent.starter.model.response.JwtResponse;
import io.github.uptalent.starter.security.JwtBlacklistService;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountUpdateVisitor accountUpdateVisitor;
    private final AccountRegisterVisitor accountRegisterVisitor;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TalentService talentService;
    private final SponsorService sponsorService;
    private final TokenEmailRepository tokenEmailRepository;
    private final EmailProducerService emailProducerService;
    private final DeletedAccountService deletedAccountService;
    private final JwtBlacklistService jwtBlackListService;
    private final AuthClient authClient;

    @Value("${email.password.ttl}")
    private Long emailPasswordTtl;
    @Value("${email.restore-account.ttl}")
    private Long emailAccountRestoreTtl;

    public AuthResponse save(AuthRegister authRegister) {
        return authRegister.accept(accountRegisterVisitor);
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmailIgnoreCase(email);
    }

    public AuthResponse login(AuthLogin authLogin) {
        String email = authLogin.getEmail();
        Account foundAccount = getAccountByEmail(email);

        if(deletedAccountService.existsByEmail(email))
            throw new BadCredentialsException("Account is deleted");

        if (!passwordEncoder.matches(authLogin.getPassword(), foundAccount.getPassword()))
            throw new BadCredentialsException("Invalid email or password");

        return generateAuthResponse(foundAccount);
    }

    public AccountProfile updateProfile(Long id, AccountUpdate accountUpdate){
        return accountUpdate.accept(id, accountUpdateVisitor);
    }

    public void deleteProfile(Long id, Role role, String accessToken) {
        Account account = getAccountByUserIdAndRole(id, role);
        EmailMessageDetailInfo emailMessageDetailInfo = generateEmailMessage(account.getEmail(), emailAccountRestoreTtl);
        String token = emailMessageDetailInfo.getUuid();
        var deletedAccount = new DeletedAccount(token, account, emailAccountRestoreTtl);

        deletedAccountService.saveTemporaryDeletedAccount(token, deletedAccount);
        emailProducerService.sendRestoreAccountMsg(emailMessageDetailInfo);
        jwtBlackListService.addToBlacklist(accessToken);
    }

    public Author getAuthor(Long id, Role role) {
        if (role.equals(Role.TALENT)) {
            return talentService.getAuthorById(id);
        } else if (role.equals(Role.SPONSOR)) {
            return sponsorService.getAuthorById(id);
        }
        throw new NoSuchRoleException();
    }

    public void changePassword(ChangePassword request, Long id, Role role) {
        Account account = getAccountByUserIdAndRole(id, role);

        if (!isSamePasswords(request.getOldPassword(), account.getPassword())) {
            throw new RuntimeException();
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    public void sendEmailToRestorePassword(String email) {
        if (!accountRepository.existsByEmailIgnoreCase(email)) {
            throw new UserNotFoundException(email);
        }

        EmailMessageDetailInfo emailMessageDetailInfo = generateEmailMessage(email, emailPasswordTtl);
        emailProducerService.sendChangePasswordMsg(emailMessageDetailInfo);
    }

    public void setNewPassword(String newPassword, String token) {
        TokenEmail tokenEmail = getTokenEmail(token);
        Account account = getAccountByEmail(tokenEmail.getEmail());

        tokenEmailRepository.deleteById(token);
        account.setPassword(passwordEncoder.encode(newPassword));
    }

    public JwtResponse restoreAccount(String token) {
        DeletedAccount deletedAccount = deletedAccountService.getTemporaryDeletedAccount(token);
        Account account = deletedAccount.getAccount();
        AuthResponse authResponse = generateAuthResponse(account);
        JwtResponse jwtResponse = authClient.loginAfterRestore(authResponse);

        tokenEmailRepository.deleteById(token);
        deletedAccountService.deleteTemporaryDeletedAccount(token);

        return jwtResponse;
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    private TokenEmail getTokenEmail(String token) {
        return tokenEmailRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private AuthResponse generateAuthResponse(Account account) {
        if (account.getRole().equals(Role.SPONSOR)) {
            Sponsor sponsor = sponsorService.getSponsorByAccount(account);

            return AuthResponse.builder()
                    .id(sponsor.getId())
                    .name(sponsor.getFullname())
                    .email(account.getEmail())
                    .role(account.getRole())
                    .build();
        } else {
            Talent talent = talentService.getTalentByAccount(account);

            return AuthResponse.builder()
                    .id(talent.getId())
                    .name(talent.getFirstname())
                    .email(account.getEmail())
                    .role(account.getRole())
                    .build();
        }
    }

    private boolean isSamePasswords(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    public Account getAccountByUserIdAndRole(Long id, Role role) {
        if (role.equals(Role.TALENT)) {
            return talentService.getTalentById(id).getAccount();
        } else {
            return sponsorService.getSponsorById(id).getAccount();
        }
    }

    private EmailMessageDetailInfo generateEmailMessage(String email, Long ttl) {
        String token = UUID.randomUUID().toString();
        String name = findAccountHolderNameByEmail(email);
        TokenEmail tokenEmail = new TokenEmail(token, email, ttl);
        EmailMessageDetailInfo emailMessageDetailInfo = new EmailMessageDetailInfo(token,
                name,
                email,
                LocalDateTime.now().plusSeconds(ttl));

        tokenEmailRepository.save(tokenEmail);
        return emailMessageDetailInfo;
    }

    public String findAccountHolderNameByEmail(String email) {
        return accountRepository.findAccountHolderNameByEmail(email);
    }
}
