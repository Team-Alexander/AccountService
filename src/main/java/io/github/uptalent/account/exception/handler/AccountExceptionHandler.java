package io.github.uptalent.account.exception.handler;

import io.github.uptalent.account.exception.InvalidAgeException;
import io.github.uptalent.account.exception.NoSuchRoleException;
import io.github.uptalent.account.exception.UserNotFoundException;
import io.github.uptalent.starter.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handlerNotFoundException(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            InvalidAgeException.class,
            NoSuchRoleException.class
    })
    public ErrorResponse handlerInvalidAgeException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handlerBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponse(e.getMessage());
    }
}
