package io.github.uptalent.account.service;

import io.github.uptalent.account.exception.SponsorNotFoundException;
import io.github.uptalent.account.mapper.SponsorMapper;
import io.github.uptalent.account.model.common.Author;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.repository.SponsorRepository;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static io.github.uptalent.starter.security.Role.SPONSOR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SponsorService {
    private final SponsorRepository sponsorRepository;
    private final SponsorMapper sponsorMapper;

    public AccountProfile getSponsorProfile(Long id, Long principalId, Role role) {
        Sponsor sponsor = getSponsorById(id);
        if ((Objects.equals(id, principalId) && role == SPONSOR) )
            return sponsorMapper.toSponsorProfile(sponsor);
        else
            return AccountProfile.builder()
                    .id(sponsor.getId())
                    .avatar(sponsor.getAvatar())
                    .name(sponsor.getFullname())
                    .build();
    }

    public Sponsor getSponsorById(Long id) {
        return sponsorRepository.findById(id).orElseThrow(SponsorNotFoundException::new);
    }

    public Sponsor getSponsorByAccount(Account account) {
        return sponsorRepository.findSponsorByAccount(account)
                .orElseThrow(SponsorNotFoundException::new);
    }

    public Author getAuthorById(long id) {
        return sponsorRepository.getAuthorById(id)
                .orElseThrow(SponsorNotFoundException::new);
    }

    public Long getSponsorBalanceById(long id) {
        return getSponsorById(id).getKudos();
    }

    @Transactional
    public void updateSponsorBalanceById(Long id, Long balance) {
        Sponsor sponsor = getSponsorById(id);
        sponsor.setKudos(balance);
    }

    @Transactional
    public void save(Sponsor sponsor) {
        sponsorRepository.save(sponsor);
    }
}
