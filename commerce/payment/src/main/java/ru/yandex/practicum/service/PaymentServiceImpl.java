package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.payment.PaymentDto;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;
import ru.yandex.practicum.enums.payment.PaymentState;
import ru.yandex.practicum.interfaces.PaymentInterface;
import ru.yandex.practicum.interfaces.PaymentRepository;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentInterface {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    @Override
    @Transactional
    public PaymentDto enrichOrderWithPayment(UUID orderId, List<OrderPaymentRequest> paymentRequests) {
        // Проверка статуса заказа OrderState происходит на стороне сервиса Order
        BigDecimal sum = paymentRequests.stream()
                .map(req -> req.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Payment payment = Payment.builder()
                .orderId(orderId)
                .productsPrice(sum)
                .tax(getTax(sum))
                .totalPrice(sum.add(getTax(sum)))
                .build();

        repository.save(payment);
        return mapper.toDto(payment);
    }

    @Override
    @Transactional
    public PaymentDto setPaymentState(UUID paymentId, PaymentState state) {
        Payment payment = repository.findById(paymentId).orElseThrow(
                () -> new NoSuchElementException(""));
        switch (state) {
            case PaymentState.PENDING -> payment.setState(PaymentState.PENDING);
            case PaymentState.SUCCESS -> payment.setState(PaymentState.SUCCESS);
            case PaymentState.FAILED -> payment.setState(PaymentState.FAILED);
        }

        repository.save(payment);
        return mapper.toDto(payment);
    }

    @Override
    public BigDecimal getTax(BigDecimal sumOfPrice) {
        return sumOfPrice.multiply(BigDecimal.valueOf(0.1))
                .setScale(2, RoundingMode.HALF_UP);
    }
}


