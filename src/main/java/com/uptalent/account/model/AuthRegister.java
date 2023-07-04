package com.uptalent.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRegister {
    private String name;
    private String role;
    private String email;
    private String password;
}
