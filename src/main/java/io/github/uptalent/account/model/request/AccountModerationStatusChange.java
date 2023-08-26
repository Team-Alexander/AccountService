package io.github.uptalent.account.model.request;

import io.github.uptalent.starter.security.Role;
import io.github.uptalent.starter.util.enums.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountModerationStatusChange {
    private Long id;
    @EnumValue(enumClass = Role.class)
    private String role;
}
