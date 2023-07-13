package com.uptalent.account.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TalentProfile extends AccountProfile {
    private String banner;
    private String location;
    private String aboutMe;
}
