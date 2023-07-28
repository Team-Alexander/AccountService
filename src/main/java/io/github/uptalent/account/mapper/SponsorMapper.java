package io.github.uptalent.account.mapper;

import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.response.SponsorProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SponsorMapper {
    @Mapping(source = "sponsor.account.email", target = "email")
    @Mapping(source = "sponsor.fullname", target = "name")
    SponsorProfile toSponsorProfile(Sponsor sponsor);
}
