package io.github.uptalent.account.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.uptalent.account.client.PerspectiveClient;
import io.github.uptalent.account.model.entity.Account;
import io.github.uptalent.account.model.entity.Report;
import io.github.uptalent.account.model.entity.Sponsor;
import io.github.uptalent.account.model.entity.Talent;
import io.github.uptalent.account.repository.AccountRepository;
import io.github.uptalent.account.repository.ReportRepository;
import io.github.uptalent.starter.model.common.Comment;
import io.github.uptalent.starter.model.common.TextItem;
import io.github.uptalent.starter.model.request.ReportRequest;
import io.github.uptalent.starter.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.uptalent.starter.model.common.EventNotificationMessage;

import java.util.Collections;
import java.util.Map;

import static io.github.uptalent.starter.model.enums.EventNotificationType.REPORT_ACCOUNT;
import static io.github.uptalent.starter.model.enums.ModerationStatus.ON_MODERATION;
import static io.github.uptalent.starter.security.Role.SPONSOR;
import static io.github.uptalent.starter.security.Role.TALENT;
import static io.github.uptalent.starter.util.Constants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final TalentService talentService;
    private final SponsorService sponsorService;
    private final EventNotificationProducerService eventNotificationProducerService;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    private final PerspectiveClient perspectiveClient;

    @Value("${perspectiveapi.key}")
    private String perspectiveApiKey;

    @Value("${admin.id}")
    private Long adminId;

    public void reportTalent(Long id, ReportRequest reportRequest) {
        Talent talent = talentService.getTalentById(id);
        reportAccount(reportRequest, talent.getAccount(), TALENTS_LINK);
    }

    public void reportSponsor(Long id, ReportRequest reportRequest) {
        Sponsor sponsor = sponsorService.getSponsorById(id);
        reportAccount(reportRequest, sponsor.getAccount(), SPONSORS_LINK);
    }

    private void reportAccount(ReportRequest reportRequest, Account account, String link) {
        account.setStatus(ON_MODERATION);

        Report report = Report.builder()
                .message(reportRequest.getMessage())
                .account(account)
                .build();

        report = reportRepository.save(report);
        account.getReports().add(report);
        accountRepository.save(account);

        sendEventNotification(link, account.getId(), report.getMessage());
    }

    private void sendEventNotification(String link, Long reportedUserId, String reason) {
        String to = adminId + ADMIN_SUFFIX;
        String messageBody = REPORT_ACCOUNT.getMessageBody().formatted(reason);
        String contentLink = link + reportedUserId.toString();

        var eventNotificationMessage = EventNotificationMessage.builder()
                .to(to)
                .message(messageBody)
                .contentLink(contentLink)
                .build();
        eventNotificationProducerService.sendEventNotificationMsg(eventNotificationMessage);
    }

    public void checkToxicity(Long id, String text, Role role) {
        double toxicityScore = getToxicityScore(text);
        if (toxicityScore > TOXICITY_THRESHOLD) {
            if (role == TALENT)
                reportTalent(id, new ReportRequest(text));
            else if (role == SPONSOR)
                reportSponsor(id, new ReportRequest(text));
        }
    }

    private double getToxicityScore(String text) {
        Comment comment =  Comment.builder()
                .languages(new String[]{"en"})
                .comment(new TextItem(text))
                .requestedAttributes(Map.of("TOXICITY", Collections.emptyMap()))
                .build();
        String response = perspectiveClient.analyzeText(perspectiveApiKey, comment);
        return parseToxicityScore(response);
    }

    @SneakyThrows
    private double parseToxicityScore(String jsonString) {
        JsonNode root = objectMapper.readTree(jsonString);
        return root.at("/attributeScores/TOXICITY/summaryScore/value").doubleValue();
    }
}
