package com.itson.profeco.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.response.NewOfferEventPayload;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Preference;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.repository.PreferenceRepository;
import com.itson.profeco.service.UserNotificationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferEventProcessorConsumer {
    private static final Logger log = LoggerFactory.getLogger(OfferEventProcessorConsumer.class);

    private final PreferenceRepository preferenceRepository;
    private final UserNotificationService userNotificationService;

    @RabbitListener(queues = RabbitConfig.QUEUE_OFFER_EVENT_PROCESSOR)
    @Transactional
    public void handleNewOfferEvent(NewOfferEventPayload offerEvent) {
        log.info("Procesando evento de nueva oferta para la tienda '{}' (ID: {}), producto: '{}'",
                offerEvent.getStoreName(), offerEvent.getStoreId(), offerEvent.getProductName());

        List<Preference> preferencesWithFavoriteStore =
                preferenceRepository.findByFavoriteStores_Id(offerEvent.getStoreId());

        if (preferencesWithFavoriteStore.isEmpty()) {
            log.info("No hay clientes que tengan la tienda ID {} como favorita.",
                    offerEvent.getStoreId());
            return;
        }

        Set<UserEntity> usersToNotify =
                preferencesWithFavoriteStore.stream().map(Preference::getCustomer)
                        .filter(java.util.Objects::nonNull).map(Customer::getUser)
                        .filter(java.util.Objects::nonNull).collect(Collectors.toSet());

        if (usersToNotify.isEmpty()) {
            log.info("No se encontraron usuarios válidos para las preferencias de la tienda ID {}.",
                    offerEvent.getStoreId());
            return;
        }

        log.info("Se encontraron {} usuarios para notificar sobre la oferta de la tienda ID {}.",
                usersToNotify.size(), offerEvent.getStoreId());

        for (UserEntity user : usersToNotify) {
            String notificationMessage = String.format(
                    "¡%s tiene una nueva oferta en '%s' por solo $%.2f!", offerEvent.getStoreName(),
                    offerEvent.getProductName(), offerEvent.getOfferPrice());


            String linkToOffer = String.format("/stores/%s/products/%s/offer-details", // Ejemplo de
                                                                                       // link
                    offerEvent.getStoreId(), offerEvent.getProductId());

            try {
                userNotificationService.createNotification(user.getId(), notificationMessage,
                        "NUEVA_OFERTA", linkToOffer);
                log.info(
                        "Notificación 'en-app' CREADA para usuario {} por oferta de la tienda ID {}.",
                        user.getId(), offerEvent.getStoreId());

            } catch (Exception e) {
                log.error("Error al crear notificación 'en-app' para usuario {}: {}", user.getId(),
                        e.getMessage(), e);
            }
        }
        log.info("Finalizado el procesamiento de notificaciones para la oferta de la tienda ID {}.",
                offerEvent.getStoreId());
    }
}
