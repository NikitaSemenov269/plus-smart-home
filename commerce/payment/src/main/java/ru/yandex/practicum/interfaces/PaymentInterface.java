package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.payment.PaymentDto;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;
import ru.yandex.practicum.enums.payment.PaymentState;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentInterface {
    PaymentDto enrichOrderWithPayment(UUID orderId, List<OrderPaymentRequest> paymentRequests);

    void setPaymentState(UUID paymentId, PaymentState state);

    BigDecimal calculateProductPrice(List<OrderPaymentRequest> paymentRequests);

    BigDecimal calculateTotalPrice(BigDecimal sumOfPrice);
}
