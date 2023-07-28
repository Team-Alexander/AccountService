package io.github.uptalent.account.exception;

public class InvalidAgeException extends RuntimeException{
    public InvalidAgeException(){
        super("Age of talent should be greater than 14 and less than 100");
    }
}