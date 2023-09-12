package io.github.uptalent.account.service;

import io.github.uptalent.account.exception.IllegalAdminActionException;
import io.github.uptalent.account.mapper.SponsorMapper;
import io.github.uptalent.account.mapper.TalentMapper;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.request.AccountModerationStatusChange;
import io.github.uptalent.account.model.response.AccountProfileReport;
import io.github.uptalent.starter.model.common.EmailMessageGeneralInfo;
import io.github.uptalent.starter.model.enums.ModerationStatus;
import io.github.uptalent.starter.pagination.PageWithMetadata;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static io.github.uptalent.starter.model.enums.ModerationStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final AccountService accountService;
    private final TalentService talentService;
    private final SponsorService sponsorService;
    private final EmailProducerService emailProducerService;
    private final RedisTemplate<String, String> redisTemplate;
    private final TalentMapper talentMapper;
    private final SponsorMapper sponsorMapper;

    private static final String BLOCKED_ACCOUNT = "blocked_account:";

    public PageWithMetadata<AccountProfileReport> getUsersWithModerationStatus(int page, int size, String status) {
        ModerationStatus moderationStatus = ModerationStatus.valueOf(status);
        int halfSize = size/2;

        Page<Talent> talents = talentService.
                getAllByAccountStatus(PageRequest.of(page, halfSize), moderationStatus);
        Page<Sponsor> sponsors = sponsorService.
                getAllByAccountStatus(PageRequest.of(page, size - halfSize), moderationStatus);

        List<AccountProfileReport> talentReports = talentMapper.toAccountProfileReports(talents.getContent());
        List<AccountProfileReport> sponsorReports = sponsorMapper.toAccountProfileReports(sponsors.getContent());

        List<AccountProfileReport> combined = new ArrayList<>(talentReports);
        combined.addAll(sponsorReports);

        long total = talents.getTotalElements() + sponsors.getTotalElements();
        int totalPages = total % size == 0 ? (int) total/size : (int) (total/size) + 1;

        return new PageWithMetadata<>(combined, totalPages);
    }

    //TODO: Change status to BLOCKED for all content
    public void blockUser(AccountModerationStatusChange accountModerationStatusChange) {
        validateUserStatus(accountModerationStatusChange, BLOCKED);

        Account account = updateAccountStatus(accountModerationStatusChange, BLOCKED);
        String email = account.getEmail();

        var emailMessage = generateEmailMessage(email);
        emailProducerService.sendBlockedAccountMsg(emailMessage);

        redisTemplate.opsForValue().set(BLOCKED_ACCOUNT + email, "");
    }

    //TODO: Change status to ACTIVE for all content
    public void unblockUser(AccountModerationStatusChange accountModerationStatusChange) {
        validateUserStatus(accountModerationStatusChange, ACTIVE);

        Account account = updateAccountStatus(accountModerationStatusChange, ACTIVE);
        String email = account.getEmail();

        var emailMessage = generateEmailMessage(email);
        emailProducerService.sendUnblockedAccountMsg(emailMessage);

        redisTemplate.delete(BLOCKED_ACCOUNT + email);
    }

    public void activateUser(AccountModerationStatusChange accountModerationStatusChange) {
        validateUserStatus(accountModerationStatusChange, ACTIVE);
        updateAccountStatus(accountModerationStatusChange, ACTIVE);
    }

    private Account updateAccountStatus(AccountModerationStatusChange accountModerationStatusChange,
                                        ModerationStatus status) {
        Long id = accountModerationStatusChange.getId();
        Role role = Role.valueOf(accountModerationStatusChange.getRole());
        Account account = accountService.getAccountByUserIdAndRole(id, role);
        account.setStatus(status);

        if(status == ACTIVE)
            account.getReports().clear();

        accountService.save(account);
        return account;
    }

    private EmailMessageGeneralInfo generateEmailMessage(String email) {
        return EmailMessageGeneralInfo.builder()
                .username(accountService.findAccountHolderNameByEmail(email))
                .email(email)
                .build();
    }

    private void validateUserStatus(AccountModerationStatusChange accountModerationStatusChange, ModerationStatus status) {
        Long id = accountModerationStatusChange.getId();
        Role role = Role.valueOf(accountModerationStatusChange.getRole());
        Account account = accountService.getAccountByUserIdAndRole(id, role);

        if (account.getStatus() == status)
            throw new IllegalAdminActionException(status);
    }
}
