package io.github.uptalent.account;

import io.github.uptalent.account.exception.IllegalAdminActionException;
import io.github.uptalent.account.mapper.SponsorMapper;
import io.github.uptalent.account.mapper.TalentMapper;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.model.response.AccountProfileReport;
import io.github.uptalent.account.service.*;
import io.github.uptalent.starter.model.enums.ModerationStatus;
import io.github.uptalent.starter.pagination.PageWithMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;

import static io.github.uptalent.account.utils.MockModelsUtil.*;
import static io.github.uptalent.starter.model.enums.ModerationStatus.*;
import static io.github.uptalent.starter.security.Role.TALENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private AccountService accountService;
    @Mock
    private TalentService talentService;
    @Mock
    private SponsorService sponsorService;
    @Mock
    private EmailProducerService emailProducerService;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private TalentMapper talentMapper;
    @Mock
    private SponsorMapper sponsorMapper;
    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("Get all users with moderation status successfully")
    public void getUsersWithModerationStatusSuccessfully() {
        int page = 1;
        int size = 5;
        String status = ON_MODERATION.name();

        Page<Talent> talentPageMock = mock(Page.class);
        Page<Sponsor> sponsorPageMock = mock(Page.class);

        when(talentService.getAllByAccountStatus(PageRequest.of(page, size/2), ModerationStatus.valueOf(status)))
                .thenReturn(talentPageMock);
        when(sponsorService.getAllByAccountStatus(PageRequest.of(page, size - size/2), ModerationStatus.valueOf(status)))
                .thenReturn(sponsorPageMock);

        when(talentPageMock.getContent()).thenReturn(Arrays.asList(new Talent(), new Talent()));
        when(sponsorPageMock.getContent()).thenReturn(Arrays.asList(new Sponsor(), new Sponsor()));
        when(talentPageMock.getTotalElements()).thenReturn(2L);
        when(sponsorPageMock.getTotalElements()).thenReturn(2L);

        when(talentMapper.toAccountProfileReports(any(List.class)))
                .thenReturn(Arrays.asList(AccountProfileReport.builder().build(), AccountProfileReport.builder().build()));
        when(sponsorMapper.toAccountProfileReports(any(List.class)))
                .thenReturn(Arrays.asList(AccountProfileReport.builder().build(), AccountProfileReport.builder().build()));

        PageWithMetadata<AccountProfileReport> result = adminService.getUsersWithModerationStatus(page, size, status);

        assertEquals(4, result.getContent().size());
        assertEquals(1, result.getTotalPages());

        verify(talentService).getAllByAccountStatus(PageRequest.of(page, size/2), ModerationStatus.valueOf(status));
        verify(sponsorService).getAllByAccountStatus(PageRequest.of(page, size - size/2), ModerationStatus.valueOf(status));
        verify(talentMapper).toAccountProfileReports(any(List.class));
        verify(sponsorMapper).toAccountProfileReports(any(List.class));
    }

    @Test
    @DisplayName("Block user successfully")
    public void blockUserSuccessfully() {
        var account = generateAccountWithStatus(ON_MODERATION);
        var accountModStatusChange = generateAccountModerationStatusChange();
        var emailMsg = generateEmailMessageGeneralInfo();
        ValueOperations<String, String> valueOperationsMock = mock(ValueOperations.class);

        when(accountService.getAccountByUserIdAndRole(eq(account.getId()), eq(TALENT))).thenReturn(account);
        when(redisTemplate.opsForValue()).thenReturn(valueOperationsMock);

        adminService.blockUser(accountModStatusChange);

        verify(accountService).save(account);
        verify(emailProducerService).sendBlockedAccountMsg(emailMsg);
        verify(redisTemplate.opsForValue()).set(eq(BLOCKED_ACCOUNT + account.getEmail()), eq(""));
    }

    @Test
    @DisplayName("Block user with status BLOCKED")
    public void blockUserWithStatusBlocked() {
        var account = generateAccountWithStatus(BLOCKED);
        var accountModStatusChange = generateAccountModerationStatusChange();

        when(accountService.getAccountByUserIdAndRole(eq(account.getId()), eq(TALENT))).thenReturn(account);

        assertThrows(IllegalAdminActionException.class,
                () -> adminService.blockUser(accountModStatusChange));
    }

    @Test
    @DisplayName("Unblock user successfully")
    public void unblockUserSuccessfully() {
        var account = generateAccountWithStatus(BLOCKED);
        var accountModStatusChange = generateAccountModerationStatusChange();
        var emailMsg = generateEmailMessageGeneralInfo();
        String email = account.getEmail();

        when(accountService.getAccountByUserIdAndRole(eq(account.getId()), eq(TALENT))).thenReturn(account);

        adminService.unblockUser(accountModStatusChange);

        verify(accountService).save(account);
        verify(emailProducerService).sendUnblockedAccountMsg(emailMsg);
        verify(redisTemplate).delete(BLOCKED_ACCOUNT + email);
    }

    @Test
    @DisplayName("Unblock user with status ACTIVE")
    public void unblockUserWithStatusActive() {
        var account = generateAccountWithStatus(ACTIVE);
        var accountModStatusChange = generateAccountModerationStatusChange();

        when(accountService.getAccountByUserIdAndRole(eq(account.getId()), eq(TALENT))).thenReturn(account);

        assertThrows(IllegalAdminActionException.class,
                () -> adminService.unblockUser(accountModStatusChange));
    }

    @Test
    @DisplayName("Activate user successfully")
    public void activateUserSuccessfully() {
        var account = generateAccountWithStatus(BLOCKED);
        var accountModStatusChange = generateAccountModerationStatusChange();

        when(accountService.getAccountByUserIdAndRole(eq(account.getId()), eq(TALENT))).thenReturn(account);

        adminService.activateUser(accountModStatusChange);

        verify(accountService).save(account);
    }

    @Test
    @DisplayName("Activate user with status ACTIVE")
    public void activateUserWithStatusActive() {
        var account = generateAccountWithStatus(ACTIVE);
        var accountModStatusChange = generateAccountModerationStatusChange();

        when(accountService.getAccountByUserIdAndRole(eq(account.getId()), eq(TALENT))).thenReturn(account);

        assertThrows(IllegalAdminActionException.class,
                () -> adminService.activateUser(accountModStatusChange));
    }
}
