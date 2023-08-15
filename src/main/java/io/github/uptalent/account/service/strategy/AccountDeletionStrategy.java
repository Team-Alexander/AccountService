package io.github.uptalent.account.service.strategy;

import io.github.uptalent.account.model.entity.Account;

public interface AccountDeletionStrategy {
    void deleteProfile(Account account);
}
