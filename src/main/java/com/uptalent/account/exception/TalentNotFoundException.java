package com.uptalent.account.exception;

public class TalentNotFoundException extends UserNotFoundException{
    public TalentNotFoundException(){
        super("Talent not found");
    }
}
