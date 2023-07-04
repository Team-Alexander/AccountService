package com.uptalent.account.service;

import com.uptalent.account.model.entity.Account;
import com.uptalent.account.model.entity.Sponsor;
import com.uptalent.account.model.entity.Talent;
import com.uptalent.account.model.enums.Role;
import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.model.response.RegisterResponse;
import com.uptalent.account.repository.AccountRepository;
import com.uptalent.account.repository.SponsorRepository;
import com.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.uptalent.account.model.enums.Role.TALENT;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final TalentRepository talentRepository;
    private final SponsorRepository sponsorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse save(AuthRegister authRegister) {
        Account account = Account.builder()
                .email(authRegister.getEmail())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .role(Role.valueOf(authRegister.getRole()))
                .build();

        account = accountRepository.save(account);

        if (authRegister.getRole().equals(TALENT.toString()))
            return registerTalent(authRegister, account);
        else
            return registerSponsor(authRegister, account);
    }

    private RegisterResponse registerTalent(AuthRegister authRegister, Account account) {
        String [] fullName = authRegister.getName().split(" ");
        Talent talent = Talent.builder()
                .lastname(fullName[0])
                .firstname(fullName[1])
                .account(account)
                .build();

        talent = talentRepository.save(talent);

        return new RegisterResponse(talent.getId(), talent.getFirstname(), account.getRole().toString());
    }

    private RegisterResponse registerSponsor(AuthRegister authRegister, Account account) {
        Sponsor sponsor = Sponsor.builder()
                .fullname(authRegister.getName())
                .account(account)
                .build();

        sponsor = sponsorRepository.save(sponsor);

        return new RegisterResponse(sponsor.getId(), sponsor.getFullname(), account.getRole().toString());
    }
}
