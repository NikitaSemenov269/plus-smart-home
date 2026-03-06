package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.payment.PaymentDto;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;
import ru.yandex.practicum.api.PaymentApi;
import ru.yandex.practicum.enums.payment.PaymentState;
import ru.yandex.practicum.interfaces.PaymentInterface;

import java.math.BigDecimal;
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
    public PaymentDto enrichOrderWithPayment(@RequestParam @NotNull UUID orderId,
                                    @RequestBody @Valid List<OrderPaymentRequest> paymentRequests) {
        log.info("Запрос на оплату заказа: {}", orderId);
        return payment.enrichOrderWithPayment(orderId, paymentRequests);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal calculateProductPrice(@Valid @RequestBody List<OrderPaymentRequest> paymentRequests) {
        log.info("Запрос на расчет стоимости товаров: {}", paymentRequests);
        return payment.calculateProductPrice(paymentRequests);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal calculateTotalPrice(@RequestBody BigDecimal sumOfPrice) {
        log.info("Запрос на расчет полной стоимости заказа");
        return payment.calculateTotalPrice(sumOfPrice);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void setPaymentFailed(@RequestBody @NotNull UUID paymentId) {
        log.info("Запрос на отметку неудачной оплаты: {}", paymentId);
        payment.setPaymentState(paymentId, PaymentState.FAILED);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void payOrder(@RequestBody @NotNull UUID paymentId) {
        log.info("Запрос на отметку успешной оплаты: {}", paymentId);
        payment.setPaymentState(paymentId, PaymentState.SUCCESS);
    }
}
