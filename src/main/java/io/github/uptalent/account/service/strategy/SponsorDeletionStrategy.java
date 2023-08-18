package io.github.uptalent.account.service.strategy;

import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.service.AccountImageService;
import io.github.uptalent.account.service.SponsorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SponsorDeletionStrategy implements AccountDeletionStrategy{
    private final SponsorService sponsorService;
    private final AccountImageService accountImageService;

    @Override
    public void deleteProfile(Account account) {
        Sponsor sponsor = sponsorService.getSponsorByAccount(account);
        sponsor.setFullname("Deleted Sponsor");
        sponsor.setAvatar(null);
        sponsorService.save(sponsor);
        accountImageService.deleteImageByUserIdAndRole(account.getId(), account.getRole());
        //TODO: delete sponsor's vacancies
    }
}
