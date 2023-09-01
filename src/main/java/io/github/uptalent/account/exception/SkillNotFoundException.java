package io.github.uptalent.account.exception;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException() {
        super("Some skills are not found");
    }
}
