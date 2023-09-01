package io.github.uptalent.account.exception.handler;

import io.github.uptalent.account.exception.*;
import io.github.uptalent.starter.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserNotFoundException.class,
            TokenNotFoundException.class,
            SkillNotFoundException.class,
    })
    public ErrorResponse handlerNotFoundException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorResponse handlerConflictException(UserAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            InvalidAgeException.class,
            NoSuchRoleException.class,
            MaxUploadSizeExceededException.class,
            InvalidImageFormatException.class
    })
    public ErrorResponse handlerBadRequestException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handlerBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponse(e.getMessage());
    }
}
