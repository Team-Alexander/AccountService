package io.github.uptalent.account.model.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AccountProfile {
    private Long id;
    private String name;
    private String avatar;
}
