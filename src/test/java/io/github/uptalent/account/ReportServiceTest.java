package io.github.uptalent.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.uptalent.account.client.PerspectiveClient;
import io.github.uptalent.account.exception.SponsorNotFoundException;
import io.github.uptalent.account.exception.TalentNotFoundException;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Report;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.repository.AccountRepository;
import io.github.uptalent.account.repository.ReportRepository;
import io.github.uptalent.account.service.EventNotificationProducerService;
import io.github.uptalent.account.service.ReportService;
import io.github.uptalent.account.service.SponsorService;
import io.github.uptalent.account.service.TalentService;
import io.github.uptalent.starter.model.common.Comment;
import io.github.uptalent.starter.model.common.EventNotificationMessage;
import io.github.uptalent.starter.model.request.ReportRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.uptalent.account.utils.MockModelsUtil.*;
import static io.github.uptalent.starter.security.Role.SPONSOR;
import static io.github.uptalent.starter.security.Role.TALENT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private TalentService talentService;
    @Mock
    private SponsorService sponsorService;
    @Mock
    private EventNotificationProducerService eventNotificationProducerService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PerspectiveClient perspectiveClient;
    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Report Talent successfully")
    public void reportTalentSuccessfully() {
        Talent talent = generateTalent();
        Long talentId = talent.getId();
        Account account = talent.getAccount();
        ReportRequest reportRequest = generateReportRequest();
        Report report = generateReport(reportRequest.getMessage(), account);

        when(talentService.getTalentById(talentId)).thenReturn(talent);
        when(accountRepository.save(account)).thenReturn(account);
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        reportService.reportTalent(talentId, reportRequest);

        verify(talentService).getTalentById(eq(talentId));
        verify(reportRepository).save(any(Report.class));
        verify(accountRepository).save(account);
        verify(eventNotificationProducerService)
                .sendEventNotificationMsg(any(EventNotificationMessage.class));
    }

    @Test
    @DisplayName("Report Talent that is not found")
    public void reportTalentThatIsNotFound() {
        Long talentId = 1L;
        ReportRequest reportRequest = generateReportRequest();

        when(talentService.getTalentById(talentId)).thenThrow(TalentNotFoundException.class);

        assertThrows(TalentNotFoundException.class,
                () -> reportService.reportTalent(talentId, reportRequest));
    }

    @Test
    @DisplayName("Report Sponsor successfully")
    public void reportSponsorSuccessfully() {
        Sponsor sponsor = generateSponsor();
        Long sponsorId = sponsor.getId();
        Account account = sponsor.getAccount();
        ReportRequest reportRequest = generateReportRequest();
        Report report = generateReport(reportRequest.getMessage(), account);

        when(sponsorService.getSponsorById(sponsorId)).thenReturn(sponsor);
        when(accountRepository.save(account)).thenReturn(account);
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        reportService.reportSponsor(sponsorId, reportRequest);

        verify(sponsorService).getSponsorById(eq(sponsorId));
        verify(reportRepository).save(any(Report.class));
        verify(accountRepository).save(account);
        verify(eventNotificationProducerService)
                .sendEventNotificationMsg(any(EventNotificationMessage.class));
    }

    @Test
    @DisplayName("Report Sponsor that is not found")
    public void reportSponsorThatIsNotFound() {
        Long sponsorId = 1L;
        ReportRequest reportRequest = generateReportRequest();

        when(sponsorService.getSponsorById(sponsorId)).thenThrow(SponsorNotFoundException.class);

        assertThrows(SponsorNotFoundException.class,
                () -> reportService.reportSponsor(sponsorId, reportRequest));
    }

    @Test
    @SneakyThrows
    @DisplayName("Check toxicity and report Talent")
    public void checkToxicityAndReportTalent() {
        Talent talent = generateTalent();
        Long talentId = talent.getId();
        Account account = talent.getAccount();
        ReportRequest reportRequest = generateReportRequest();
        Report report = generateReport(reportRequest.getMessage(), account);
        String text = "toxic message";
        String json = generateJsonToxicityScore(0.9f);
        JsonNode jsonNode = new ObjectMapper().readTree(json);

        when(talentService.getTalentById(talentId)).thenReturn(talent);
        when(accountRepository.save(account)).thenReturn(account);
        when(perspectiveClient.analyzeText(isNull(), any(Comment.class))).thenReturn(json);
        when(objectMapper.readTree(json)).thenReturn(jsonNode);
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        reportService.checkToxicity(talentId, text, TALENT);

        verify(talentService).getTalentById(eq(talentId));
        verify(reportRepository).save(any(Report.class));
        verify(accountRepository).save(account);
        verify(eventNotificationProducerService)
                .sendEventNotificationMsg(any(EventNotificationMessage.class));
    }

    @Test
    @SneakyThrows
    @DisplayName("Check toxicity and don't report Talent")
    public void checkToxicityAndDontReportTalent() {
        Long talentId = 1L;
        String text = "not toxic message";
        String json = generateJsonToxicityScore(0.1f);
        JsonNode jsonNode = new ObjectMapper().readTree(json);

        when(perspectiveClient.analyzeText(isNull(), any(Comment.class))).thenReturn(json);
        when(objectMapper.readTree(json)).thenReturn(jsonNode);

        reportService.checkToxicity(talentId, text, TALENT);

        verifyNoMoreInteractions(talentService, accountRepository,
                reportRepository, eventNotificationProducerService);
    }

    @Test
    @SneakyThrows
    @DisplayName("Check toxicity and report Sponsor")
    public void checkToxicityAndReportSponsor() {
        Sponsor sponsor = generateSponsor();
        Long sponsorId = sponsor.getId();
        Account account = sponsor.getAccount();
        ReportRequest reportRequest = generateReportRequest();
        Report report = generateReport(reportRequest.getMessage(), account);
        String text = "toxic message";
        String json = generateJsonToxicityScore(0.9f);
        JsonNode jsonNode = new ObjectMapper().readTree(json);

        when(sponsorService.getSponsorById(sponsorId)).thenReturn(sponsor);
        when(accountRepository.save(account)).thenReturn(account);
        when(perspectiveClient.analyzeText(isNull(), any(Comment.class))).thenReturn(json);
        when(objectMapper.readTree(json)).thenReturn(jsonNode);
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        reportService.checkToxicity(sponsorId, text, SPONSOR);

        verify(sponsorService).getSponsorById(eq(sponsorId));
        verify(reportRepository).save(any(Report.class));
        verify(accountRepository).save(account);
        verify(eventNotificationProducerService)
                .sendEventNotificationMsg(any(EventNotificationMessage.class));
    }

    @Test
    @SneakyThrows
    @DisplayName("Check toxicity and don't report Sponsor")
    public void checkToxicityAndDontReportSponsor() {
        Long sponsorId = 1L;
        String text = "not toxic message";
        String json = generateJsonToxicityScore(0.1f);
        JsonNode jsonNode = new ObjectMapper().readTree(json);

        when(perspectiveClient.analyzeText(isNull(), any(Comment.class))).thenReturn(json);
        when(objectMapper.readTree(json)).thenReturn(jsonNode);

        reportService.checkToxicity(sponsorId, text, SPONSOR);

        verifyNoMoreInteractions(sponsorService, accountRepository,
                reportRepository, eventNotificationProducerService);
    }
}
