package io.github.uptalent.account.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillRequest {
    @NotEmpty(message = "Skill ids list must not be empty")
    @NotNull(message = "Skill ids list must not contain null values")
    private Set<@NotNull(message = "Skill Id must not be null") Long> skillIds;
}
