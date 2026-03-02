package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.model.Delivery;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryInterface {

    DeliveryResponse createDeliveryRequest(DeliveryRequest dto);

    BigDecimal calculateDelivery(UUID deliveryId);

    BigDecimal calculateDelivery(Delivery delivery);

    void payToDelivery(UUID deliveryId);

    DeliveryResponse setDeliveryState(UUID deliveryId, DeliveryState state);
}
