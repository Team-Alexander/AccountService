package io.github.uptalent.account.service;

import io.github.uptalent.account.exception.NoSuchRoleException;
import io.github.uptalent.account.model.common.Author;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.enums.Role;
import io.github.uptalent.account.model.request.AuthLogin;
import io.github.uptalent.account.model.request.AuthRegister;
import io.github.uptalent.account.repository.AccountRepository;
import io.github.uptalent.account.repository.SponsorRepository;
import io.github.uptalent.account.service.strategy.SponsorDeletionStrategy;
import io.github.uptalent.account.service.strategy.TalentDeletionStrategy;
import io.github.uptalent.account.service.visitor.AccountRegisterVisitor;
import io.github.uptalent.account.service.visitor.AccountUpdateVisitor;
import io.github.uptalent.account.model.request.AccountUpdate;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.model.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountUpdateVisitor accountUpdateVisitor;
    private final AccountRegisterVisitor accountRegisterVisitor;
    private final AccountSecurityService accountSecurityService;
    private final TalentDeletionStrategy talentDeletionStrategy;
    private final SponsorDeletionStrategy sponsorDeletionStrategy;
    private final AccountRepository accountRepository;
    private final SponsorRepository sponsorRepository;
    private final PasswordEncoder passwordEncoder;
    private final TalentService talentService;
    private final SponsorService sponsorService;

    public AuthResponse save(AuthRegister authRegister) {
        return authRegister.accept(accountRegisterVisitor);
    }

    public AuthResponse login(AuthLogin authLogin) {
        String email = authLogin.getEmail();
        Account foundAccount = accountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Account with email [" + email + "] does not exist"));

        if (!passwordEncoder.matches(authLogin.getPassword(), foundAccount.getPassword()))
            throw new BadCredentialsException("Invalid email or password");

        return generateAuthResponse(foundAccount);
    }

    public AccountProfile updateProfile(Long id, AccountUpdate accountUpdate){
        return accountUpdate.accept(id, accountUpdateVisitor);
    }

    public void deleteProfile(Long id) {
        Role role = accountSecurityService.getRoleFromAuthorities();

        if (role.equals(Role.TALENT)) {
            talentDeletionStrategy.deleteProfile(id);
        } else if (role.equals(Role.SPONSOR)) {
            sponsorDeletionStrategy.deleteProfile(id);
        }
    }

    public Author getAuthor() {
        Long id = accountSecurityService.getIdFromPrincipal();
        Role role = accountSecurityService.getRoleFromAuthorities();

        if (role.equals(Role.TALENT)) {
            return talentService.getAuthorById(id);
        } else if (role.equals(Role.SPONSOR)) {
            return sponsorService.getAuthorById(id);
        }
        throw new NoSuchRoleException();
    }

    private AuthResponse generateAuthResponse(Account account) {
        if (account.getRole().equals(Role.SPONSOR)) {
            Sponsor sponsor = sponsorService.getSponsorByAccount(account);

            return new AuthResponse(
                    sponsor.getId(),
                    sponsor.getFullname(),
                    Role.SPONSOR.name()
            );
        } else {
            Talent talent = talentService.getTalentByAccount(account);

            return new AuthResponse(
                    talent.getId(),
                    talent.getFirstname(),
                    Role.TALENT.name()
            );
        }
    }
}
