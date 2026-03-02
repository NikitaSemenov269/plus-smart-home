package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;
import ru.yandex.practicum.api.DeliveryApi;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.interfaces.DeliveryInterface;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryApi {
    private final DeliveryInterface delivery;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public DeliveryResponse createDelivery(@RequestBody @Valid
                                           DeliveryRequest request) {
        log.info("Запрос на создание доставки для заказа: {}", request.getOrderId());
        return delivery.createDeliveryRequest(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/cost")
    public BigDecimal calculateDelivery(@RequestParam @NotNull UUID deliveryId) {
        log.info("Запрос на расчет стоимости доставки для заказа: {}", deliveryId);
        return delivery.calculateDelivery(deliveryId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/successful")
    public void setDeliverySuccessful(@RequestParam @NotNull UUID deliveryId) {
        log.info("Запрос на изменение статуса \"успешная доставка\": {}", deliveryId);
        delivery.setDeliveryState(deliveryId, DeliveryState.DELIVERED);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/failed")
    public void setDeliveryFailed(@RequestParam @NotNull UUID deliveryId) {
        log.info("Запрос на изменение статуса \"не успешная доставка\": {}", deliveryId);
        delivery.setDeliveryState(deliveryId, DeliveryState.FAILED);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pay")
    public void payToDelivery(@RequestParam @NotNull UUID deliveryId) {
        log.info("Запрос на оплату доставки: {}", deliveryId);
        delivery.payToDelivery(deliveryId);
    }
}
