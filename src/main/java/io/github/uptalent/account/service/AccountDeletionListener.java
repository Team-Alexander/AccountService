package io.github.uptalent.account.service;

import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.hash.DeletedAccount;
import io.github.uptalent.account.service.strategy.SponsorDeletionStrategy;
import io.github.uptalent.account.service.strategy.TalentDeletionStrategy;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.uptalent.starter.security.Role.SPONSOR;
import static io.github.uptalent.starter.security.Role.TALENT;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountDeletionListener implements MessageListener {
    private static final String DELETED_ACCOUNT_KEY = "deleted_account";
    private final AccountService accountService;
    private final TalentDeletionStrategy talentDeletionStrategy;
    private final SponsorDeletionStrategy sponsorDeletionStrategy;
    private final DeletedAccountService deletedAccountService;

    @Async
    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody());
        String[] parts = key.split(":");
        if(!parts[0].equals(DELETED_ACCOUNT_KEY))
            return;

        String token = parts[1];
        DeletedAccount deletedAccount = deletedAccountService.getTemporaryDeletedAccount(token);
        if (deletedAccount == null)
            return;

        Account account = deletedAccount.getAccount();
        Role role = account.getRole();

        if (role == TALENT) {
            talentDeletionStrategy.deleteProfile(account);
        } else if (role == SPONSOR) {
            sponsorDeletionStrategy.deleteProfile(account);
        }

        account.setEmail("deleted");
        accountService.save(account);
        deletedAccountService.deleteTemporaryDeletedAccount(token);
    }
}