package com.uptalent.account.model.request;

import com.uptalent.account.service.visitor.AccountUpdateVisitor;
import com.uptalent.account.model.response.AccountProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TalentUpdate extends AccountUpdate{
    @NotBlank(message = "Lastname should not be blank")
    @Size(max = 15, message = "Lastname must be less than 15 characters")
    private String lastname;

    @NotBlank(message = "Firstname should not be blank")
    @Size(max = 15, message = "Firstname must be less than 15 characters")
    private String firstname;

    private LocalDate birthday;

    @Size(max = 255, message = "Location should be less than 255 characters")
    private String location;

    @Size(max = 2250, message = "About me should be less than 2250 characters")
    private String aboutMe;

    public AccountProfile accept(Long id, AccountUpdateVisitor visitor) {
        return visitor.updateProfile(id, this);
    }
}
