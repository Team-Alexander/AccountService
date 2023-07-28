package io.github.uptalent.account.exception;

public class NoSuchRoleException extends RuntimeException{
    public NoSuchRoleException(){
        super("No such role");
    }
}
