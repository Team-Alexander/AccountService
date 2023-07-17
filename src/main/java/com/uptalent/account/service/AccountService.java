package com.uptalent.account.service;

import com.uptalent.account.exception.SponsorNotFoundException;
import com.uptalent.account.exception.TalentNotFoundException;
import com.uptalent.account.model.entity.Account;
import com.uptalent.account.model.entity.Sponsor;
import com.uptalent.account.model.entity.Talent;
import com.uptalent.account.model.enums.Role;
import com.uptalent.account.model.request.AuthLogin;
import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.repository.AccountRepository;
import com.uptalent.account.repository.SponsorRepository;
import com.uptalent.account.repository.TalentRepository;
import com.uptalent.account.service.strategy.SponsorDeletionStrategy;
import com.uptalent.account.service.strategy.TalentDeletionStrategy;
import com.uptalent.account.service.visitor.AccountRegisterVisitor;
import com.uptalent.account.service.visitor.AccountUpdateVisitor;
import com.uptalent.account.model.request.AccountUpdate;
import com.uptalent.account.model.response.AccountProfile;
import com.uptalent.account.model.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.uptalent.account.model.enums.Role.SPONSOR;
import static com.uptalent.account.model.enums.Role.TALENT;

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
    private final TalentRepository talentRepository;
    private final PasswordEncoder passwordEncoder;

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

        if (role.equals(TALENT)) {
            talentDeletionStrategy.deleteProfile(id);
        } else if (role.equals(SPONSOR)) {
            sponsorDeletionStrategy.deleteProfile(id);
        }
    }

    private AuthResponse generateAuthResponse(Account account) {
        if (account.getRole().equals(SPONSOR)) {
            Sponsor sponsor = sponsorRepository.findSponsorByAccount(account)
                    .orElseThrow(SponsorNotFoundException::new);

            return new AuthResponse(
                    sponsor.getId(),
                    sponsor.getFullname(),
                    SPONSOR.name()
            );
        } else {
            Talent talent = talentRepository.findTalentByAccount(account)
                    .orElseThrow(TalentNotFoundException::new);

            return new AuthResponse(
                    talent.getId(),
                    talent.getFirstname(),
                    TALENT.name()
            );
        }
    }
}
