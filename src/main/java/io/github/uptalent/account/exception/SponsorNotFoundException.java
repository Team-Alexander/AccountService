package io.github.uptalent.account.exception;

import io.github.uptalent.starter.security.Role;

public class SponsorNotFoundException extends UserNotFoundException{
    public SponsorNotFoundException(){
        super(Role.SPONSOR);
    }
}