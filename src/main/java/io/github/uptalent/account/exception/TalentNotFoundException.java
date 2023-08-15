package io.github.uptalent.account.exception;

import io.github.uptalent.starter.security.Role;

public class TalentNotFoundException extends UserNotFoundException {
    public TalentNotFoundException(){
        super(Role.TALENT);
    }
}
