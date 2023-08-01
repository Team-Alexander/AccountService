package io.github.uptalent.account.client;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.uptalent.account.model.request.AuthorUpdate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("content-service")
@Retry(name = "default")
public interface ContentClient {
    @PostMapping("/api/v1/content/proofs/author")
    void updateProofsByAuthor(@RequestBody AuthorUpdate authorUpdate);

    @DeleteMapping("/api/v1/content/proofs/author")
    void deleteProofsByAuthor();
}
