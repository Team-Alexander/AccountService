package io.github.uptalent.account.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.queue.change.password}")
    private String changePasswordQueue;
    @Value("${rabbitmq.routing-key.change.password}")
    private String changePasswordRoutingKey;
    @Value("${rabbitmq.queue.restore-account}")
    private String restoreAccountQueue;
    @Value("${rabbitmq.routing-key.restore-account}")
    private String restoreAccountRoutingKey;

    @Bean
    public Queue changePasswordQueue() {
        return new Queue(changePasswordQueue);
    }

    @Bean
    public Queue restoreAccountQueue() {
        return new Queue(restoreAccountQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding changePasswordBinding() {
        return BindingBuilder
                .bind(changePasswordQueue())
                .to(exchange())
                .with(changePasswordRoutingKey);
    }

    @Bean
    public Binding restoreAccountBinding() {
        return BindingBuilder
                .bind(restoreAccountQueue())
                .to(exchange())
                .with(restoreAccountRoutingKey);
    }
}