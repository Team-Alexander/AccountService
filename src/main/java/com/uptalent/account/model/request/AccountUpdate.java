package com.uptalent.account.model.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.uptalent.account.service.visitor.AccountUpdateVisitor;
import com.uptalent.account.model.response.AccountProfile;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TalentUpdate.class, name = "talentUpdate"),
        @JsonSubTypes.Type(value = SponsorUpdate.class, name = "sponsorUpdate")
})
public abstract class AccountUpdate {
    public abstract AccountProfile accept(Long id, AccountUpdateVisitor visitor);
}
