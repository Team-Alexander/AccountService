package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.common.Author;
import io.github.uptalent.account.model.request.AuthLogin;
import io.github.uptalent.account.model.request.AuthRegister;
import io.github.uptalent.account.model.request.ChangePassword;
import io.github.uptalent.account.model.response.AuthResponse;
import io.github.uptalent.account.service.AccountService;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static io.github.uptalent.starter.util.Constants.USER_ID_KEY;
import static io.github.uptalent.starter.util.Constants.USER_ROLE_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse save(@RequestBody AuthRegister authRegister) {
        return accountService.save(authRegister);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody AuthLogin authLogin) {
        return accountService.login(authLogin);
    }

    @GetMapping("/author")
    public Author getAuthor(@RequestHeader(USER_ID_KEY) Long id,
                            @RequestHeader(USER_ROLE_KEY) Role role) {
        return accountService.getAuthor(id, role);
    }

    @PatchMapping("/password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestHeader(USER_ID_KEY) Long id,
                               @RequestHeader(USER_ROLE_KEY) Role role,
                               @RequestBody ChangePassword request) {
        accountService.changePassword(request, id, role);
    }

    @PostMapping("/password/email")
    public void sendEmailToRestorePassword(@RequestBody String email) {
        accountService.sendEmailToRestorePassword(email);
    }

    @PostMapping("/password/new")
    public void setNewPassword(@RequestBody String newPassword,
                               @RequestParam String token) {
        accountService.setNewPassword(newPassword, token);
    }
}
