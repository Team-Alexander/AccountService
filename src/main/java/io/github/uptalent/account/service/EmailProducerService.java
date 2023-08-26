package io.github.uptalent.account.service;

import io.github.uptalent.starter.model.common.EmailMessageDetailInfo;
import io.github.uptalent.starter.model.common.EmailMessageGeneralInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key.change.password}")
    private String changePasswordRoutingKey;
    @Value("${rabbitmq.routing-key.restore-account}")
    private String restoreAccountRoutingKey;
    @Value("${rabbitmq.routing-key.blocked-account}")
    private String blockedAccountRoutingKey;
    @Value("${rabbitmq.routing-key.unblocked-account}")
    private String unblockedAccountRoutingKey;

    public void sendChangePasswordMsg(EmailMessageDetailInfo message) {
        rabbitTemplate.convertAndSend(exchange, changePasswordRoutingKey, message);
    }

    public void sendRestoreAccountMsg(EmailMessageDetailInfo message) {
        rabbitTemplate.convertAndSend(exchange, restoreAccountRoutingKey, message);
    }

    public void sendBlockedAccountMsg(EmailMessageGeneralInfo message) {
        rabbitTemplate.convertAndSend(exchange, blockedAccountRoutingKey, message);
    }

    public void sendUnblockedAccountMsg(EmailMessageGeneralInfo emailMessage) {
        rabbitTemplate.convertAndSend(exchange, unblockedAccountRoutingKey, emailMessage);
    }
}
