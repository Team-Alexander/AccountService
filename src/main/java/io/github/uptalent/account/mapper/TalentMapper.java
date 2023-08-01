package io.github.uptalent.account.mapper;

import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.request.AuthorUpdate;
import io.github.uptalent.account.model.response.TalentFullProfile;
import io.github.uptalent.account.model.response.TalentProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TalentMapper {
    @Mapping(source = "talent.account.email", target = "email")
    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    TalentFullProfile toTalentFullProfile(Talent talent);

    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    TalentProfile toTalentProfile(Talent talent);

    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    AuthorUpdate toAuthorUpdate(Talent talent);
}
