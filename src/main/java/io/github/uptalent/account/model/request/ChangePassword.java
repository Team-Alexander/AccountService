package io.github.uptalent.account.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePassword {
    @NotBlank(message = "Old password should not be blank")
    @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
    private String oldPassword;
    @NotBlank(message = "New password should not be blank")
    @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
    private String newPassword;
}
