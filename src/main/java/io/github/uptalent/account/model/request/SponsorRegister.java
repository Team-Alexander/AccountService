package io.github.uptalent.account.model.request;

import io.github.uptalent.account.model.response.AuthResponse;
import io.github.uptalent.account.service.visitor.AccountRegisterVisitor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SponsorRegister extends AuthRegister{
    @NotBlank(message = "Full name should not be blank")
    @Size(max = 30, message = "Full name must be less than 30 characters")
    private String fullname;

    public AuthResponse accept(AccountRegisterVisitor visitor) {
        return visitor.registerProfile(this);
    }
}