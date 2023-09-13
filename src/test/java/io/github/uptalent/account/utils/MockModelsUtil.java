package io.github.uptalent.account.utils;

import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Skill;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.request.AccountModerationStatusChange;
import io.github.uptalent.account.model.request.SkillRequest;
import io.github.uptalent.account.model.response.AccountProfileReport;
import io.github.uptalent.account.model.response.SkillResponse;
import io.github.uptalent.starter.model.common.EmailMessageGeneralInfo;
import io.github.uptalent.starter.model.enums.ModerationStatus;
import io.github.uptalent.starter.pagination.PageWithMetadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.uptalent.starter.security.Role.TALENT;

public final class MockModelsUtil {
    private MockModelsUtil() {}

    public static final String BLOCKED_ACCOUNT = "blocked_account:";

    public static PageWithMetadata<AccountProfileReport>  generateUsersWithModerationStatus() {
        List<AccountProfileReport> users = List.of(
                AccountProfileReport.builder()
                        .id(1L)
                        .name("Test")
                        .avatar("avatar")
                        .build(),
                AccountProfileReport.builder()
                        .id(2L)
                        .name("Test")
                        .avatar("avatar")
                        .build(),
                AccountProfileReport.builder()
                        .id(3L)
                        .name("Test")
                        .avatar("avatar")
                        .build()
        );

        return new PageWithMetadata<>(users, 1);
    }

    public static AccountModerationStatusChange generateAccountModerationStatusChange() {
        return new AccountModerationStatusChange(1L, TALENT.name());
    }

    public static Account generateAccountWithStatus(ModerationStatus status) {
        return Account.builder()
                .id(1L)
                .email("test@email.com")
                .password("password")
                .role(TALENT)
                .status(status)
                .reports(new ArrayList<>())
                .build();
    }

    public static EmailMessageGeneralInfo generateEmailMessageGeneralInfo() {
        return new EmailMessageGeneralInfo(null, "test@email.com");
    }

    public static List<Skill> generateSkills() {
        return List.of(
                Skill.builder()
                        .id(1L)
                        .name("Java")
                        .build(),
                Skill.builder()
                        .id(2L)
                        .name("Python")
                        .build()
        );
    }

    public static List<SkillResponse> generateSkillResponses() {
        return List.of(
                SkillResponse.builder()
                        .id(1L)
                        .name("Java")
                        .build(),
                SkillResponse.builder()
                        .id(2L)
                        .name("Python")
                        .build()
        );
    }

    public static SkillRequest  generateSkillRequest() {
        return new SkillRequest(Set.of(1L, 2L));
    }

    public static Talent generateTalent() {
        return Talent.builder()
                .id(1L)
                .firstname("Test")
                .lastname("Test")
                .avatar("avatar")
                .skills(new HashSet<>(generateSkills()))
                .build();
    }
}
