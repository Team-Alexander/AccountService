package com.uptalent.account.service;

import com.uptalent.account.exception.SponsorNotFoundException;
import com.uptalent.account.mapper.SponsorMapper;
import com.uptalent.account.model.entity.Sponsor;
import com.uptalent.account.model.response.AccountProfile;
import com.uptalent.account.repository.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SponsorService {
    private final SponsorRepository sponsorRepository;
    private final AccountSecurityService accountSecurityService;
    private final SponsorMapper sponsorMapper;

    public AccountProfile getSponsorProfile(Long id) {
        Sponsor sponsor = getSponsorById(id);
        if (accountSecurityService.isPersonalProfile(id))
            return sponsorMapper.toSponsorProfile(sponsor);
        else
            return AccountProfile.builder()
                    .id(sponsor.getId())
                    .avatar(sponsor.getAvatar())
                    .name(sponsor.getFullname())
                    .build();
    }

    private Sponsor getSponsorById(Long id) {
        return sponsorRepository.findById(id).orElseThrow(SponsorNotFoundException::new);
    }
}
