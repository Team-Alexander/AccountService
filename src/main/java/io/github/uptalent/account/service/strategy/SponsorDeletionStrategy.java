package io.github.uptalent.account.service.strategy;

import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.repository.SponsorRepository;
import io.github.uptalent.account.service.SponsorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SponsorDeletionStrategy implements AccountDeletionStrategy{
    private final SponsorService sponsorService;

    @Override
    public void deleteProfile(Account account) {
        Sponsor sponsor = sponsorService.getSponsorByAccount(account);
        sponsor.setFullname("Deleted Sponsor");
        //TODO: delete avatar from S3 bucket
        sponsor.setAvatar(null);
        sponsorService.save(sponsor);
        //TODO: delete sponsor's vacancies
    }
}
