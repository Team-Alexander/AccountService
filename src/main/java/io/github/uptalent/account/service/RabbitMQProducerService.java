package io.github.uptalent.account.service;

import io.github.uptalent.account.model.common.SendEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing-key.name}")
    private String routingKey;

    public void sendMessage(SendEmailMessage message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
