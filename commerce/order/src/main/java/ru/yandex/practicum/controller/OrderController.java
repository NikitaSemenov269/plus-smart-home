package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.order.CreateNewOrderRequest;
import ru.yandex.practicum.DTO.order.OrderDto;
import ru.yandex.practicum.DTO.order.ProductReturnRequest;
import ru.yandex.practicum.api.OrderApi;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.interfaces.OrderService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService service;

    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createNewOrder(@NotBlank String username,
                                   @Valid CreateNewOrderRequest dto) {
        return service.createNewOrder(username, dto);
    }

    @ResponseStatus(HttpStatus.OK)
    public Page<OrderDto> getOrdersOfUser(@NotBlank String username,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getOrdersOfUser(username, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    public void payForTheOrder(@NotNull UUID orderId) {
        service.payForTheOrder(orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    public OrderDto orderRefund(@NotNull ProductReturnRequest productReturnRequest) {
        return service.orderRefund(productReturnRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    public void setOrderState(@NotNull UUID orderId, @NotNull OrderState state) {
        service.setOrderState(orderId, state);
    }

    @Override
    public BigDecimal getTotalPrice(@NotNull UUID orderId) {
        return service.getTotalPrice(orderId);
    }

    @Override
    public OrderDto assembleOrder(@NotNull UUID orderId) {
        return service.setOrderState(orderId, OrderState.ASSEMBLED);
    }

    @Override
    public OrderDto assembleOrderFailed(@NotNull UUID orderId) {
        return service.setOrderState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    @Override
    public BigDecimal getDeliveryPrice(@NotNull UUID orderId) {
        return service.getDeliveryPrice(orderId);
    }

    @Override
    public OrderDto deliveryOrder(@NotNull UUID orderId) {
        return service.setOrderState(orderId, OrderState.DELIVERED);
    }

    @Override
    public OrderDto deliveryOrderFailed(@NotNull UUID orderId) {
        return service.setOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto completedOrder(@NotNull UUID orderId) {
        return service.setOrderState(orderId, OrderState.COMPLETED);
    }
}
