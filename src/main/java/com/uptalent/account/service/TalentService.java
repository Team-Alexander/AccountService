package com.uptalent.account.service;

import com.uptalent.account.exception.TalentNotFoundException;
import com.uptalent.account.mapper.TalentMapper;
import com.uptalent.account.model.entity.Talent;
import com.uptalent.account.model.enums.Role;
import com.uptalent.account.model.response.AccountProfile;
import com.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.uptalent.account.model.enums.Role.SPONSOR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalentService {
    private final TalentRepository talentRepository;
    private final AccountSecurityService accountSecurityService;
    private final TalentMapper talentMapper;

    public AccountProfile getTalentProfile(Long id) {
        Talent talent = getTalentById(id);
        Role role = accountSecurityService.getRoleFromAuthorities();

        if (accountSecurityService.isPersonalProfile(id) || SPONSOR.equals(role))
            return talentMapper.toTalentFullProfile(talent);
        else
            return talentMapper.toTalentProfile(talent);
    }

    private Talent getTalentById(Long id){
        return talentRepository.findById(id).orElseThrow(TalentNotFoundException::new);
    }
}
