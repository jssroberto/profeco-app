package com.itson.profeco.service;

import com.itson.profeco.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMessageConsumerService {

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
        // TODO: Add logic here
    }
}
