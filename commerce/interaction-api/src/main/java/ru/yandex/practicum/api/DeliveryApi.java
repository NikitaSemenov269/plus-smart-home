package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryApi {
    @PostMapping
    DeliveryResponse createDelivery(@RequestBody DeliveryRequest request);

    @PostMapping("/cost")
    BigDecimal calculateDelivery(@RequestParam UUID deliveryId);

    @PutMapping("/successful")
    void setDeliverySuccessful(@RequestParam  UUID deliveryId);

    @PutMapping("/failed")
    void setDeliveryFailed(@RequestParam  UUID deliveryId);

    @PostMapping("/pay")
    void payToDelivery(@RequestParam  UUID deliveryId);
}