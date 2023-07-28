package io.github.uptalent.account.model.request;

import io.github.uptalent.account.service.visitor.AccountUpdateVisitor;
import io.github.uptalent.account.model.response.AccountProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SponsorUpdate extends AccountUpdate{
    @NotBlank(message = "Full name should not be blank")
    @Size(max = 30, message = "Full name must be less than 30 characters")
    private String fullname;

    public AccountProfile accept(Long id, AccountUpdateVisitor visitor) {
        return visitor.updateProfile(id, this);
    }
}
