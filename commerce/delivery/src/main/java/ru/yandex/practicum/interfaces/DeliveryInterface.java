package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;
import ru.yandex.practicum.enums.delivery.DeliveryState;

import java.util.UUID;

public interface DeliveryInterface {

    DeliveryResponse createDeliveryRequest(DeliveryRequest dto);

    DeliveryResponse payToDelivery(UUID deliveryId);

    DeliveryResponse setDeliveryState(UUID deliveryId, DeliveryState state);
}
