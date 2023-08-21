package io.github.uptalent.account.client;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.uptalent.account.model.common.JwtResponse;
import io.github.uptalent.account.model.response.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("uptalent-auth")
@Retry(name = "default")
public interface AuthClient {
    @PostMapping("/api/v1/auth/loginAfterRestore")
    JwtResponse loginAfterRestore(AuthResponse authResponse);
}
