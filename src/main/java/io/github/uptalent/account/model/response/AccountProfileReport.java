package io.github.uptalent.account.model.response;

import io.github.uptalent.starter.model.response.ReportResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountProfileReport extends AccountProfile {
    private List<ReportResponse> reports;
}
