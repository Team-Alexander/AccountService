package io.github.uptalent.account.exception;

public class SponsorNotFoundException extends UserNotFoundException{
    public SponsorNotFoundException(){
        super("Sponsor not found");
    }
}