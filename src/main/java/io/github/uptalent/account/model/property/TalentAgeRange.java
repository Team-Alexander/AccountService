package io.github.uptalent.account.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TalentAgeRange {
    private int minAge;
    private int maxAge;
}
