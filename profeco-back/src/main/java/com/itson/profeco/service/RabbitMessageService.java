package com.itson.profeco.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.itson.profeco.config.RabbitConfig;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RabbitMessageService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY,
                message);
    }
}
