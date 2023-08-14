package io.github.uptalent.account.service.strategy;

import io.github.uptalent.account.client.ContentClient;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TalentDeletionStrategy implements AccountDeletionStrategy{
    private final TalentService talentService;
    private final ContentClient contentClient;

    @Override
    public void deleteProfile(Account account) {
        Talent talent = talentService.getTalentByAccount(account);
        talent.setFirstname("Deleted");
        talent.setLastname("Talent");
        //TODO: delete avatar and banner from S3 bucket
        talent.setAvatar(null);
        talent.setBanner(null);
        talentService.save(talent);
        contentClient.deleteProofsByAuthor(true, talent.getId());
    }
}
