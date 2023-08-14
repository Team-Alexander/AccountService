package io.github.uptalent.account.model.hash;

import io.github.uptalent.account.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "deleted_account")
public class DeletedAccount implements Serializable {
    @Id
    private String token;
    private Account account;
    @TimeToLive
    private Long ttl;
}
