package io.github.uptalent.account.service;

import io.github.uptalent.account.model.common.EmailMessageDetailInfo;
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

    public void sendChangePasswordMsg(EmailMessageDetailInfo message) {
        rabbitTemplate.convertAndSend(exchange, changePasswordRoutingKey, message);
    }

    public void sendRestoreAccountMsg(EmailMessageDetailInfo message) {
        rabbitTemplate.convertAndSend(exchange, restoreAccountRoutingKey, message);
    }
}
