package io.github.uptalent.account.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SponsorProfile extends AccountProfile {
    private String email;
    private long kudos;
}
