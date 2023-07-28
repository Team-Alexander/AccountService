package io.github.uptalent.account.service.visitor;

import io.github.uptalent.account.model.request.SponsorUpdate;
import io.github.uptalent.account.model.request.TalentUpdate;
import io.github.uptalent.account.model.response.AccountProfile;

public interface AccountUpdateVisitor {
    AccountProfile updateProfile(Long id, TalentUpdate talentUpdate);
    AccountProfile updateProfile(Long id, SponsorUpdate sponsorUpdate);
}
