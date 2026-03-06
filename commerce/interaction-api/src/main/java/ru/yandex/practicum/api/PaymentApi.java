package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.DTO.payment.PaymentDto;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentApi {
    @PostMapping
    PaymentDto enrichOrderWithPayment(@RequestParam UUID orderId,
                             @RequestBody List<OrderPaymentRequest> paymentRequests);

    @PostMapping("/productCost")
    BigDecimal calculateProductPrice(@RequestBody List<OrderPaymentRequest> paymentRequests);

    @PostMapping("/totalCost")
    BigDecimal calculateTotalPrice(@RequestBody BigDecimal sumOfPrice);

    @PostMapping("/failed")
    void setPaymentFailed(@RequestBody UUID paymentId);

    @PostMapping("/refund")
    void payOrder(@RequestBody UUID paymentId);
}
