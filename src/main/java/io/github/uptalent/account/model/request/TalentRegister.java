package io.github.uptalent.account.model.request;

import io.github.uptalent.account.model.response.AuthResponse;
import io.github.uptalent.account.service.visitor.AccountRegisterVisitor;
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
public class TalentRegister extends AuthRegister {
    @NotBlank(message = "Lastname should not be blank")
    @Size(max = 15, message = "Lastname must be less than 15 characters")
    private String firstname;

    @NotBlank(message = "Firstname should not be blank")
    @Size(max = 15, message = "Firstname must be less than 15 characters")
    private String lastname;

    public AuthResponse accept(AccountRegisterVisitor visitor) {
        return visitor.registerProfile(this);
    }
}
