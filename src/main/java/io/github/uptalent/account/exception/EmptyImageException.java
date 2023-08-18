package io.github.uptalent.account.exception;

public class EmptyImageException extends RuntimeException {
    public EmptyImageException() {
        super("Image must not be empty");
    }
}
