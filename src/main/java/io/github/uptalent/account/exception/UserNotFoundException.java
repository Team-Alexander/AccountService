package io.github.uptalent.account.exception;

import io.github.uptalent.starter.security.Role;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super(String.format("Account with email [%s] does not exist", email));
    }

    public UserNotFoundException(Role role){
        super(role.toString() + " not found");
    }
}
