package com.itson.profeco.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitConfig {
    // --- Constantes para Notificaciones Individuales ---
    public static final String QUEUE_NOTIFICATIONS = "notificaciones.queue";
    public static final String EXCHANGE_NOTIFICATIONS = "notificaciones.exchange";
    public static final String ROUTING_KEY_NOTIFICATION_NEW = "notificacion.nueva"; // Usado por el producer
    public static final String BINDING_PATTERN_NOTIFICATIONS = "notificacion.#";    // Usado por el binding del listener

    // --- Constantes para Eventos de Nuevas Ofertas ---
    public static final String EXCHANGE_NEW_OFFERS_FANOUT = "offers.new.fanout.exchange";
    public static final String QUEUE_OFFER_EVENT_PROCESSOR = "offers.event.processor.queue";

    // --- Beans para Notificaciones Individuales ---
    @Bean
    public Queue notificationsQueue() { // Renombrado para claridad y evitar colisión de nombres de método
        return new Queue(QUEUE_NOTIFICATIONS, true);
    }

    @Bean
    public TopicExchange notificationsExchange() { // Renombrado para claridad
        return new TopicExchange(EXCHANGE_NOTIFICATIONS);
    }

    @Bean
    public Binding notificationsBinding(
            // Los nombres de los parámetros coinciden con los nombres de los métodos @Bean de arriba
            Queue notificationsQueue,
            TopicExchange notificationsExchange) {
        return BindingBuilder.bind(notificationsQueue).to(notificationsExchange).with(BINDING_PATTERN_NOTIFICATIONS);
    }

    // --- Beans para Eventos de Nuevas Ofertas ---
    @Bean
    public FanoutExchange newOffersFanoutExchange() {
        return new FanoutExchange(EXCHANGE_NEW_OFFERS_FANOUT);
    }

    @Bean
    public Queue offerEventProcessorQueue() {
        return new Queue(QUEUE_OFFER_EVENT_PROCESSOR, true);
    }

    @Bean
    public Binding offerEventProcessorBinding(
            // Los nombres de los parámetros coinciden con los nombres de los métodos @Bean de arriba
            Queue offerEventProcessorQueue,
            FanoutExchange newOffersFanoutExchange) {
        return BindingBuilder.bind(offerEventProcessorQueue).to(newOffersFanoutExchange);
    }

    // --- Beans Comunes ---
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}