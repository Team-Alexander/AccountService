package io.github.uptalent.account.service;

import io.github.uptalent.account.model.common.EmailMessageInfo;
import io.github.uptalent.account.model.common.SendEmailMessage;
import io.github.uptalent.account.model.constant.EmailMessageLinkType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.github.uptalent.account.model.constant.EmailMessageConstant.*;

@Service
@RequiredArgsConstructor
public class EmailMessageService {
    private static final String TOKEN_PARAMETER = "?token=";
    private static final String DEFAULT_SUBJECT = "Default subject";
    private static final String DEFAULT_MESSAGE = "Default message";

    private final RabbitMQProducerService rabbitMQProducerService;

    @Value("${client.url}")
    private String clientUrl;

    public void sendMessage(EmailMessageInfo messageInfo) {
        EmailMessageLinkType messageLinkType = messageInfo.getMessageLinkType();
        String link = generateLink(messageInfo.getUuid(), messageLinkType);
        String message = generateLetter(messageInfo.getUsername(),
                messageInfo.getExpiredDateTime(),
                link,
                messageLinkType);
        String subject = getEmailSubjectByMessageLinkType(messageLinkType);
        String email = messageInfo.getEmail();
        SendEmailMessage sendEmailMessage = new SendEmailMessage(email, subject, message);

        rabbitMQProducerService.sendMessage(sendEmailMessage);
    }


    private String generateLetter(String username,
                                  LocalDateTime expiredDateTime,
                                  String link,
                                  EmailMessageLinkType messageType) {
        String messageBody = getEmailMessageByMessageLinkType(messageType);

        return MESSAGE_HEADER +
                messageBody.formatted(username, expiredDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE), link) +
                MESSAGE_FOOTER;
    }

    private String generateLink(String uuid, EmailMessageLinkType messageType) {
        return clientUrl +
                messageType.getUrl() +
                TOKEN_PARAMETER +
                uuid;
    }

    private String getEmailSubjectByMessageLinkType(EmailMessageLinkType messageLinkType) {
        switch (messageLinkType) {
            case CHANGE -> {
                return SUBJECT_CHANGE_PASSWORD;
            }
            case VERIFY -> {
                return SUBJECT_VERIFY;
            }
            case RESTORE -> {
                return SUBJECT_RESTORE;
            }
            default -> {
                return DEFAULT_SUBJECT;
            }
        }
    }

    private String getEmailMessageByMessageLinkType(EmailMessageLinkType messageLinkType) {
        switch (messageLinkType) {
            case CHANGE -> {
                return MESSAGE_CHANGE_PASSWORD;
            }
            case VERIFY -> {
                return MESSAGE_VERIFY;
            }
            case RESTORE -> {
                return MESSAGE_RESTORE;
            }
            default -> {
                return DEFAULT_MESSAGE;
            }
        }
    }
}
