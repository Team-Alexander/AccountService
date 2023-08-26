package io.github.uptalent.account.mapper;

import io.github.uptalent.account.model.entity.Report;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.request.AuthorUpdate;
import io.github.uptalent.account.model.response.AccountProfileReport;
import io.github.uptalent.account.model.response.SponsorProfile;
import io.github.uptalent.starter.model.response.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SponsorMapper {
    @Mapping(source = "sponsor.account.email", target = "email")
    @Mapping(source = "sponsor.fullname", target = "name")
    SponsorProfile toSponsorProfile(Sponsor sponsor);

    ReportResponse toReportResponse(Report report);

    @Mapping(source = "sponsor.fullname", target = "name")
    @Mapping(source = "sponsor.account.reports", target = "reports")
    AccountProfileReport toAccountProfileReport(Sponsor sponsor);

    List<AccountProfileReport> toAccountProfileReports(List<Sponsor> sponsors);

    @Mapping(source = "sponsor.fullname", target = "name")
    AuthorUpdate toAuthorUpdate(Sponsor sponsor);
}
