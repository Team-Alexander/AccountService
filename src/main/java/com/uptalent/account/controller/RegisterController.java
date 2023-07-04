package com.uptalent.account.controller;

import com.uptalent.account.model.AuthRegister;
import com.uptalent.account.model.AuthResponse;
import com.uptalent.account.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/save")
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping
    public AuthResponse save(@RequestBody AuthRegister authRegister) {
        return registerService.save(authRegister);
    }
}
