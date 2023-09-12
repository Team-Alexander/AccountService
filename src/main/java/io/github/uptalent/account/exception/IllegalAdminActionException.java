package io.github.uptalent.account.exception;

import io.github.uptalent.starter.model.enums.ModerationStatus;

public class IllegalAdminActionException extends RuntimeException {
    public IllegalAdminActionException(ModerationStatus status) {
        super("User is already " + status.name().toLowerCase());
    }
}
