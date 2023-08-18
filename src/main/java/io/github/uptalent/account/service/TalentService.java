package io.github.uptalent.account.service;

import io.github.uptalent.account.exception.TalentNotFoundException;
import io.github.uptalent.account.mapper.TalentMapper;
import io.github.uptalent.account.model.common.Author;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.repository.TalentRepository;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static io.github.uptalent.starter.security.Role.SPONSOR;
import static io.github.uptalent.starter.security.Role.TALENT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalentService {
    private final TalentRepository talentRepository;
    private final TalentMapper talentMapper;

    public AccountProfile getTalentProfile(Long id, Long principalId, Role role) {
        Talent talent = getTalentById(id);

        if ((Objects.equals(id, principalId) && role == TALENT) || role == SPONSOR)
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

    @Transactional
    public void save(Talent talent) {
        talentRepository.save(talent);
    }

    public Optional<String> findAvatarById(Long id) {
        return talentRepository.findAvatarById(id);
    }

    public Optional<String> findBannerById(Long id) {
        return talentRepository.findBannerById(id);
    }
}
