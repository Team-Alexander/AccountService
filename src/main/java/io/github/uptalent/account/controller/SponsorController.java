package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.request.SponsorUpdate;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.service.AccountService;
import io.github.uptalent.account.service.SponsorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/sponsors")
public class SponsorController {
    private final AccountService accountService;
    private final SponsorService sponsorService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public AccountProfile getSponsorProfile(@PathVariable Long id){
        return sponsorService.getSponsorProfile(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#id == authentication.principal and hasAuthority('SPONSOR')")
    public AccountProfile updateSponsor(@PathVariable Long id,
                                        @RequestBody @Valid SponsorUpdate sponsorUpdate){
        return accountService.updateProfile(id, sponsorUpdate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal and hasAuthority('SPONSOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTalent(@PathVariable Long id){
        accountService.deleteProfile(id);
    }
}
