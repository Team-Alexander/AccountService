package com.uptalent.account.controller;

import com.uptalent.account.model.request.SponsorUpdate;
import com.uptalent.account.model.response.AccountProfile;
import com.uptalent.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/sponsors")
public class SponsorController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal and hasAuthority('SPONSOR')")
    public AccountProfile getSponsorProfile(@PathVariable Long id){
        //TODO implement
        return null;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#id == authentication.principal and hasAuthority('SPONSOR')")
    public AccountProfile updateSponsor(@PathVariable Long id,
                                        @RequestBody @Valid SponsorUpdate sponsorUpdate){
        return accountService.updateProfile(id, sponsorUpdate);
    }
}
