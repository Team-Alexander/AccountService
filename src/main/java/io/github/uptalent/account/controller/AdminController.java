package io.github.uptalent.account.controller;

import io.github.uptalent.account.model.request.AccountModerationStatusChange;
import io.github.uptalent.account.model.response.AccountProfileReport;
import io.github.uptalent.account.service.AdminService;
import io.github.uptalent.starter.model.enums.ModerationStatus;
import io.github.uptalent.starter.pagination.PageWithMetadata;
import io.github.uptalent.starter.util.enums.EnumValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public PageWithMetadata<AccountProfileReport> getUsersWithModerationStatus(
            @Min(value = 0, message = "Page should be greater or equals 0")
            @RequestParam(defaultValue = "0") int page,
            @Positive(message = "Size should be positive")
            @RequestParam(defaultValue = "9") int size,
            @EnumValue(enumClass = ModerationStatus.class)
            @RequestParam(required = false, defaultValue = "ON_MODERATION") String status){
        return adminService.getUsersWithModerationStatus(page, size, status);
    }

    @PostMapping("/block")
    public void blockUser(@RequestBody @Valid AccountModerationStatusChange accountModerationStatusChange) {
        adminService.blockUser(accountModerationStatusChange);
    }

    @PostMapping("/unblock")
    public void unblockUser(@RequestBody @Valid AccountModerationStatusChange accountModerationStatusChange) {
        adminService.unblockUser(accountModerationStatusChange);
    }

    @PostMapping("/activate")
    public void activateUser(@RequestBody @Valid AccountModerationStatusChange accountModerationStatusChange) {
        adminService.activateUser(accountModerationStatusChange);
    }
}
