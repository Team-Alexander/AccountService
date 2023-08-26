package io.github.uptalent.account.client;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.uptalent.account.model.request.AuthorUpdate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("uptalent-content")
@Retry(name = "default")
public interface ContentClient {
    @PatchMapping("/api/v1/content/proofs/author")
    void updateProofsByAuthor(@RequestBody AuthorUpdate authorUpdate);

    @DeleteMapping("/api/v1/content/proofs/author/{userId}")
    void deleteProofsByAuthor(@RequestHeader(value = "skipInterceptor") boolean skipInterceptor,
                              @PathVariable Long userId);

    @DeleteMapping("/api/v1/content/vacancies/author/{userId}")
    void deleteVacanciesByAuthor(@RequestHeader(value = "skipInterceptor") boolean skipInterceptor,
                                 @PathVariable Long userId);

    @PatchMapping("/api/v1/content/vacancies/author")
    void updateVacanciesByAuthor(@RequestBody AuthorUpdate authorUpdate);
}
