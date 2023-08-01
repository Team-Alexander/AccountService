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

import static io.github.uptalent.starter.util.Constants.USER_ID_KEY;

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

    @PatchMapping
    @PreAuthorize("hasAuthority('SPONSOR')")
    public AccountProfile updateSponsor(@RequestHeader(USER_ID_KEY) Long id,
                                        @RequestBody @Valid SponsorUpdate sponsorUpdate){
        return accountService.updateProfile(id, sponsorUpdate);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('SPONSOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSponsor(@RequestHeader(USER_ID_KEY) Long id){
        accountService.deleteProfile(id);
    }
}
