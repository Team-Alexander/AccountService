package com.uptalent.account.exception;

public class SponsorNotFoundException extends UserNotFoundException{
    public SponsorNotFoundException(){
        super("Sponsor not found");
    }
}