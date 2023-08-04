package io.github.uptalent.account.service.visitor;

import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.enums.Role;
import io.github.uptalent.account.model.request.AuthRegister;
import io.github.uptalent.account.model.request.SponsorRegister;
import io.github.uptalent.account.model.request.TalentRegister;
import io.github.uptalent.account.model.response.AuthResponse;
import io.github.uptalent.account.repository.AccountRepository;
import io.github.uptalent.account.repository.SponsorRepository;
import io.github.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRegisterVisitorImpl implements AccountRegisterVisitor{
    private final TalentRepository talentRepository;
    private final SponsorRepository sponsorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse registerProfile(TalentRegister talentRegister) {
        Account account = saveAccount(talentRegister, Role.TALENT);
        Talent talent = Talent.builder()
                .lastname(talentRegister.getLastname())
                .firstname(talentRegister.getFirstname())
                .account(account)
                .build();

        talent = talentRepository.save(talent);

        return AuthResponse.builder()
                .id(talent.getId())
                .name(talent.getFirstname())
                .email(account.getEmail())
                .role(Role.TALENT)
                .build();
    }

    @Override
    public AuthResponse registerProfile(SponsorRegister sponsorRegister) {
        Account account = saveAccount(sponsorRegister, Role.SPONSOR);
        Sponsor sponsor = Sponsor.builder()
                .fullname(sponsorRegister.getFullname())
                .account(account)
                .kudos(50L)
                .build();

        sponsor = sponsorRepository.save(sponsor);

        return AuthResponse.builder()
                .id(sponsor.getId())
                .name(sponsor.getFullname())
                .email(account.getEmail())
                .role(Role.SPONSOR)
                .build();
    }

    private Account saveAccount(AuthRegister authRegister, Role role){
        Account account = Account.builder()
                .email(authRegister.getEmail())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .role(role)
                .build();

        return accountRepository.save(account);
    }
}
