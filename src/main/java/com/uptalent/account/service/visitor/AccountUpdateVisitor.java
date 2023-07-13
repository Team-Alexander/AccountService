package com.uptalent.account.service.visitor;

import com.uptalent.account.model.request.SponsorUpdate;
import com.uptalent.account.model.request.TalentUpdate;
import com.uptalent.account.model.response.AccountProfile;

public interface AccountUpdateVisitor {
    AccountProfile updateProfile(Long id, TalentUpdate talentUpdate);
    AccountProfile updateProfile(Long id, SponsorUpdate sponsorUpdate);
}
