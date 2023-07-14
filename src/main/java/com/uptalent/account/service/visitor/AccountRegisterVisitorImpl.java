package com.uptalent.account.service.visitor;

import com.uptalent.account.model.entity.Account;
import com.uptalent.account.model.entity.Sponsor;
import com.uptalent.account.model.entity.Talent;
import com.uptalent.account.model.enums.Role;
import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.model.request.SponsorRegister;
import com.uptalent.account.model.request.TalentRegister;
import com.uptalent.account.model.response.AuthResponse;
import com.uptalent.account.repository.AccountRepository;
import com.uptalent.account.repository.SponsorRepository;
import com.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.uptalent.account.model.enums.Role.SPONSOR;
import static com.uptalent.account.model.enums.Role.TALENT;

@Service
@RequiredArgsConstructor
public class AccountRegisterVisitorImpl implements AccountRegisterVisitor{
    private final TalentRepository talentRepository;
    private final SponsorRepository sponsorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse registerProfile(TalentRegister talentRegister) {
        Account account = saveAccount(talentRegister, TALENT);
        Talent talent = Talent.builder()
                .lastname(talentRegister.getLastname())
                .firstname(talentRegister.getFirstname())
                .account(account)
                .build();

        talent = talentRepository.save(talent);

        return new AuthResponse(talent.getId(), talent.getFirstname(), account.getRole().toString());
    }

    @Override
    public AuthResponse registerProfile(SponsorRegister sponsorRegister) {
        Account account = saveAccount(sponsorRegister, SPONSOR);
        Sponsor sponsor = Sponsor.builder()
                .fullname(sponsorRegister.getFullname())
                .build();

        sponsor = sponsorRepository.save(sponsor);

        return new AuthResponse(sponsor.getId(), sponsor.getFullname(), account.getRole().toString());
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
