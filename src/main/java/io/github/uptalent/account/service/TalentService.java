package io.github.uptalent.account.service;

import io.github.uptalent.account.exception.TalentNotFoundException;
import io.github.uptalent.account.mapper.TalentMapper;
import io.github.uptalent.account.model.common.Author;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.enums.Role;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.repository.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (accountSecurityService.isPersonalProfile(id) || Role.SPONSOR.equals(role))
            return talentMapper.toTalentFullProfile(talent);
        else
            return talentMapper.toTalentProfile(talent);
    }

    public Talent getTalentById(Long id){
        return talentRepository.findById(id).orElseThrow(TalentNotFoundException::new);
    }

    public Talent getTalentByAccount(Account account) {
        return talentRepository.findTalentByAccount(account)
                .orElseThrow(TalentNotFoundException::new);
    }

    public Author getAuthorById(long id) {
        return talentRepository.getAuthorById(id)
                .orElseThrow(TalentNotFoundException::new);
    }
}
