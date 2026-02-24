package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.payment.PaymentDto;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentInterface {
    PaymentDto enrichOrderWithPayment(UUID orderId, List<OrderPaymentRequest> paymentRequests);

    BigDecimal getTax(BigDecimal sumOfPrice);
}
