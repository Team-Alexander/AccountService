package com.uptalent.account.exception.handler;

import com.uptalent.account.exception.InvalidAgeException;
import com.uptalent.account.exception.NoSuchRoleException;
import com.uptalent.account.exception.UserNotFoundException;
import com.uptalent.account.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.uptalent.account.util.constant.AccountConstant.ACCESS_DENIED_MESSAGE;

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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handlerAccessDeniedException() {
        return new ErrorResponse(ACCESS_DENIED_MESSAGE);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handlerBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponse(e.getMessage());
    }
}
