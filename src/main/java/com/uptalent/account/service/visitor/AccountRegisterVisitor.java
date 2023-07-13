package com.uptalent.account.service.visitor;

import com.uptalent.account.model.request.SponsorRegister;
import com.uptalent.account.model.request.TalentRegister;
import com.uptalent.account.model.response.AuthResponse;

public interface AccountRegisterVisitor {
    AuthResponse registerProfile(TalentRegister talentRegister);
    AuthResponse registerProfile(SponsorRegister sponsorRegister);
}
