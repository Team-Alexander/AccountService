package com.uptalent.account.client;

import com.uptalent.account.model.response.PublicKeyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("auth-service")
public interface AuthClient {
    @GetMapping("/api/v1/auth/public-key")
    PublicKeyDTO getPublicKey();
}
