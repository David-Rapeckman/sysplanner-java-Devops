package com.sys.sysplanner.mq;

import com.sys.sysplanner.service.EmailService;
import com.sys.sysplanner.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receive(EmailMessage message) {
        emailService.sendEmail(message.getTo(), message.getSubject(), message.getBody());
    }
}