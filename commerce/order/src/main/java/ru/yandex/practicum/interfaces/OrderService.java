package ru.yandex.practicum.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.DTO.order.CreateNewOrderRequest;
import ru.yandex.practicum.DTO.order.OrderDto;
import ru.yandex.practicum.DTO.order.ProductReturnRequest;
import ru.yandex.practicum.enums.order.OrderState;

import java.util.UUID;


public interface OrderService {

    OrderDto createNewOrder(CreateNewOrderRequest dto);

    OrderDto payForTheOrder(UUID orderId);

    Page<OrderDto> getOrdersOfUser(String username, Pageable pageable);

    OrderDto orderRefund(ProductReturnRequest productReturnRequest);

    OrderDto setOrderState(UUID orderId, OrderState state);
}

