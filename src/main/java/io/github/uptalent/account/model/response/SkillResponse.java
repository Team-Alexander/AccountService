package io.github.uptalent.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SkillResponse {
    private long id;
    private String name;
}
