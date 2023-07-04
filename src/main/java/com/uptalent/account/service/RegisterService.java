package com.uptalent.account.service;

import com.uptalent.account.model.AuthRegister;
import com.uptalent.account.model.AuthResponse;
import com.uptalent.account.model.Talent;
import com.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final TalentRepository talentRepository;

    // Only talents register
    // TODO: make opportunity register sponsors to service
    @Transactional
    public AuthResponse save(AuthRegister authRegister) {
        String [] fullname = authRegister.getName().split(" ");
        Talent talent = Talent.builder()
                .lastname(fullname[0])
                .firstname(fullname[1])
                .email(authRegister.getEmail())
                .password(authRegister.getPassword())
                .role(authRegister.getRole())
                .build();

        talent = talentRepository.save(talent);

        return new AuthResponse(talent.getId(), talent.getFirstname(), talent.getRole());
    }

    public List<Talent> retrieveTalents() {
        return talentRepository.findAll();
    }
}
