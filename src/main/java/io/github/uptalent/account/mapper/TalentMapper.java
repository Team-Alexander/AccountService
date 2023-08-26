package io.github.uptalent.account.mapper;

import io.github.uptalent.account.model.entity.Report;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.request.AuthorUpdate;
import io.github.uptalent.account.model.response.*;
import io.github.uptalent.starter.model.response.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface TalentMapper {
    @Named("toFullProfile")
    @Mapping(source = "talent.account.email", target = "email")
    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    TalentFullProfile toTalentFullProfile(Talent talent);

    @Named("toProfile")
    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    TalentProfile toTalentProfile(Talent talent);

    List<TalentProfile> toTalentProfiles(List<Talent> talents);

    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    AuthorUpdate toAuthorUpdate(Talent talent);

    ReportResponse toReportResponse(Report report);

    @Mapping(target = "name", expression = "java(talent.getFirstname() + \" \" + talent.getLastname())")
    @Mapping(source = "talent.account.reports", target = "reports")
    AccountProfileReport toAccountProfileReport(Talent talent);

    List<AccountProfileReport> toAccountProfileReports(List<Talent> content);
}
