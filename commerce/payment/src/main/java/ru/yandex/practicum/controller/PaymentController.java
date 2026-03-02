package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.order.OrderDto;
import ru.yandex.practicum.DTO.payment.PaymentDto;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;
import ru.yandex.practicum.api.PaymentApi;
import ru.yandex.practicum.interfaces.PaymentInterface;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {
    private final PaymentInterface payment;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pay")
    public PaymentDto createPayment(@RequestParam @NotNull UUID orderId,
                                    @RequestBody List<OrderPaymentRequest> paymentRequests) {
        log.info("Запрос оплату заказа: {}", orderId);
        return payment.enrichOrderWithPayment(orderId, paymentRequests);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Double calculateProductPrice(@Valid @RequestBody OrderDto orderDto) {
        log.info("Запрос на расчет стоимости товаров для заказа: {}", orderDto.getOrderId());
        return paymentService.calculateProductPrice(orderDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Double calculateTotalPrice(@Valid @RequestBody OrderDto orderDto) {
        log.info("Запрос на расчет полной стоимости заказа: {}", orderDto.getOrderId());
        return paymentService.calculateTotalPrice(orderDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void setPaymentFailed(@PathVariable UUID paymentId) {
        log.info("Запрос на отметку неудачной оплаты: {}", paymentId);
        paymentService.setPaymentFailed(paymentId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void payOrder(@PathVariable UUID paymentId) {
        log.info("Запрос на подтверждение успешной оплаты: {}", paymentId);
        paymentService.payOrder(paymentId);
    }
}
