package io.github.uptalent.account.model.hash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "token_email")
public class TokenEmail {
    @Id
    private String token;
    private String email;
    @TimeToLive
    private Long ttl;
}
