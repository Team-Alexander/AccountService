package io.github.uptalent.account.service;

import io.github.uptalent.account.model.hash.DeletedAccount;
import io.github.uptalent.account.repository.DeletedAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DeletedAccountService {
    private final DeletedAccountRepository deletedAccountRepository;
    private final RedisTemplate<String, DeletedAccount> redisAccountTemplate;
    private final RedisTemplate<String, String> redisEmailTemplate;
    private static final String DELETED_ACCOUNT_KEY = "deleted_account_cache";
    private static final String DELETED_ACCOUNT_EMAIL_KEY = "deleted_account_email_cache:";

    @Value("${email.restore-account.ttl}")
    private Long emailAccountRestoreTtl;

    public void saveTemporaryDeletedAccount(String token, DeletedAccount deletedAccount) {
        String email = deletedAccount.getAccount().getEmail();
        String key = DELETED_ACCOUNT_EMAIL_KEY + email;

        redisAccountTemplate.opsForHash().put(DELETED_ACCOUNT_KEY, token, deletedAccount);
        redisEmailTemplate.opsForValue().set(key, "");
        redisEmailTemplate.expire(key, emailAccountRestoreTtl, TimeUnit.SECONDS);
        deletedAccountRepository.save(deletedAccount);
    }

    public void deleteTemporaryDeletedAccount(String token) {
        String key = DELETED_ACCOUNT_EMAIL_KEY + getTemporaryDeletedAccount(token).getAccount().getEmail();

        redisAccountTemplate.opsForHash().delete(DELETED_ACCOUNT_KEY, token);
        redisEmailTemplate.delete(key);
        deletedAccountRepository.deleteById(token);
    }

    public DeletedAccount getTemporaryDeletedAccount(String token) {
        return (DeletedAccount) redisAccountTemplate.opsForHash().get(DELETED_ACCOUNT_KEY, token);
    }

    public boolean existsByEmail(String email) {
        String key = DELETED_ACCOUNT_EMAIL_KEY + email;
        return Boolean.TRUE.equals(redisEmailTemplate.hasKey(key));
    }
}