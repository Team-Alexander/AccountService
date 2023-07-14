package com.uptalent.account.controller;

import com.uptalent.account.model.request.AuthLogin;
import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.model.response.AuthResponse;
import com.uptalent.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
