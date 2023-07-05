package com.uptalent.account.controller;

import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.model.response.AuthResponse;
import com.uptalent.account.service.AccountSecurityService;
import com.uptalent.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountSecurityService accountSecurityService;

    @PostMapping("/save")
    public AuthResponse save(@RequestBody AuthRegister authRegister) {
        return accountService.save(authRegister);
    }

    @GetMapping("/me")
    public Long get() {
        return accountSecurityService.getIdFromPrincipal();
    }
}
