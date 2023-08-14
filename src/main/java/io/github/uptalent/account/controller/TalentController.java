package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.request.TalentUpdate;
import io.github.uptalent.account.model.response.AccountProfile;
import io.github.uptalent.account.service.AccountService;
import io.github.uptalent.account.service.TalentService;
import io.github.uptalent.starter.security.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static io.github.uptalent.starter.util.Constants.USER_ID_KEY;
import static io.github.uptalent.starter.util.Constants.USER_ROLE_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/talents")
public class TalentController {
    private final AccountService accountService;
    private final TalentService talentService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public AccountProfile getTalentProfile(@PathVariable Long id,
                                           @RequestHeader(USER_ID_KEY) Long principalId,
                                           @RequestHeader(USER_ROLE_KEY) Role role) {
        return talentService.getTalentProfile(id, principalId, role);
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('TALENT')")
    public AccountProfile updateTalent(@RequestHeader(USER_ID_KEY) Long id,
                                       @RequestBody @Valid TalentUpdate talentUpdate){
        return accountService.updateProfile(id, talentUpdate);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('TALENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTalent(@RequestHeader(USER_ID_KEY) Long id,
                             @RequestHeader(USER_ROLE_KEY) Role role){
        accountService.deleteProfile(id, role);
    }
}
