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
    @Value("${rabbitmq.queue.restore-account}")
    private String restoreAccountQueue;
    @Value("${rabbitmq.queue.event_notification}")
    private String eventNotificationQueue;
    @Value("${rabbitmq.queue.blocked-account}")
    private String blockedAccountQueue;
    @Value("${rabbitmq.queue.unblocked-account}")
    private String unblockedAccountQueue;
    @Value("${rabbitmq.routing-key.change.password}")
    private String changePasswordRoutingKey;
    @Value("${rabbitmq.routing-key.restore-account}")
    private String restoreAccountRoutingKey;
    @Value("${rabbitmq.routing-key.event_notification}")
    private String eventNotificationRoutingKey;
    @Value("${rabbitmq.routing-key.blocked-account}")
    private String blockedAccountRoutingKey;
    @Value("${rabbitmq.routing-key.unblocked-account}")
    private String unblockedAccountRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    public Queue createQueue(String queueName) {
        return new Queue(queueName);
    }

    public Binding createBinding(Queue queue, String routingKey){
        return BindingBuilder.bind(queue).to(exchange()).with(routingKey);
    }

    @Bean
    public Queue changePasswordQueue() {
        return createQueue(changePasswordQueue);
    }

    @Bean
    public Queue restoreAccountQueue() {
        return createQueue(restoreAccountQueue);
    }

    @Bean
    public Queue eventNotificationQueue() {
        return createQueue(eventNotificationQueue);
    }

    @Bean
    public Queue blockedAccountQueue() {
        return createQueue(blockedAccountQueue);
    }

    @Bean
    public Queue unblockedAccountQueue() {
        return createQueue(unblockedAccountQueue);
    }

    @Bean
    public Binding changePasswordBinding() {
        return createBinding(changePasswordQueue(), changePasswordRoutingKey);
    }

    @Bean
    public Binding restoreAccountBinding() {
        return createBinding(restoreAccountQueue(), restoreAccountRoutingKey);
    }

    @Bean
    public Binding eventNotificationBinding() {
        return createBinding(eventNotificationQueue(), eventNotificationRoutingKey);
    }

    @Bean
    public Binding blockedAccountBinding() {
        return createBinding(blockedAccountQueue(), blockedAccountRoutingKey);
    }

    @Bean
    public Binding unblockedAccountBinding() {
        return createBinding(unblockedAccountQueue(), unblockedAccountRoutingKey);
    }
}