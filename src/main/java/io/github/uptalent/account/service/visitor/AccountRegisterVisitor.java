package io.github.uptalent.account.service.visitor;

import io.github.uptalent.account.model.request.SponsorRegister;
import io.github.uptalent.account.model.request.TalentRegister;
import io.github.uptalent.account.model.response.AuthResponse;

public interface AccountRegisterVisitor {
    AuthResponse registerProfile(TalentRegister talentRegister);
    AuthResponse registerProfile(SponsorRegister sponsorRegister);
}
