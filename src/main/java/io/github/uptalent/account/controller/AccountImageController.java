package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.enums.ImageType;
import io.github.uptalent.account.service.AccountImageService;
import io.github.uptalent.starter.security.Role;
import io.github.uptalent.starter.util.enums.EnumValue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static io.github.uptalent.starter.util.Constants.USER_ID_KEY;
import static io.github.uptalent.starter.util.Constants.USER_ROLE_KEY;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/images")
public class AccountImageController {
    private final AccountImageService accountImageService;

    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    public void uploadImage(@RequestHeader(USER_ID_KEY) Long id,
                            @RequestHeader(USER_ROLE_KEY) Role role,
                            @RequestParam
                            MultipartFile image,
                            @RequestParam(required = false, defaultValue = "AVATAR")
                            @EnumValue(enumClass = ImageType.class) String imageType){
        accountImageService.uploadImage(id, role, image, imageType);
    }
}
