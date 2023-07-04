package com.uptalent.account.controller;

import com.uptalent.account.model.request.AuthRegister;
import com.uptalent.account.model.response.RegisterResponse;
import com.uptalent.account.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/save")
    public RegisterResponse save(@RequestBody AuthRegister authRegister) {
        return registerService.save(authRegister);
    }

    @GetMapping("/me")
    public String get() {
        return "authorized";
    }
}
