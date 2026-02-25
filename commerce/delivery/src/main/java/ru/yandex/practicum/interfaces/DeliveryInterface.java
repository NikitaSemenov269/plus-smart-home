package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;

import java.util.UUID;

public interface DeliveryInterface {

    DeliveryResponse createDeliveryRequest(DeliveryRequest dto);

    DeliveryResponse payToDelivery(UUID deliveryId);
}
