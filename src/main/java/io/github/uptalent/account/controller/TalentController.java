package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.request.TalentUpdate;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.service.AccountService;
import io.github.uptalent.account.service.TalentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/talents")
public class TalentController {
    private final AccountService accountService;
    private final TalentService talentService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public AccountProfile getTalentProfile(@PathVariable Long id){
        return talentService.getTalentProfile(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("#id == authentication.principal and hasAuthority('TALENT')")
    public AccountProfile updateTalent(@PathVariable Long id,
                                       @RequestBody @Valid TalentUpdate talentUpdate){
        return accountService.updateProfile(id, talentUpdate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal and hasAuthority('TALENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTalent(@PathVariable Long id){
        accountService.deleteProfile(id);
    }
}
