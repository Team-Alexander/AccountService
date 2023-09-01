package io.github.uptalent.account.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TalentProfile extends AccountProfile {
    private String banner;
    private String location;
    private String aboutMe;
    private List<SkillResponse> skills;
}
