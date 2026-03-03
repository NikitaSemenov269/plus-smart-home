package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.order.CreateNewOrderRequest;
import ru.yandex.practicum.DTO.order.OrderDto;
import ru.yandex.practicum.DTO.order.ProductReturnRequest;
import ru.yandex.practicum.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderApi {
    @PutMapping
    OrderDto createNewOrder(@RequestParam(name = "username") String username,
                            @RequestBody CreateNewOrderRequest newOrderRequest);

    @GetMapping
    Page<OrderDto> getOrdersOfUser(@RequestParam String username,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size);

    @PostMapping("/payment")
    void payForTheOrder(UUID orderId);

    @PostMapping("/return")
    OrderDto orderRefund(ProductReturnRequest productReturnRequest);

    @PostMapping("/payment/failed")
    void setOrderState(UUID orderId, OrderState state);

    @PostMapping("/calculate/total")
    BigDecimal getTotalPrice(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembleOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assembleOrderFailed(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    BigDecimal getDeliveryPrice(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryOrder(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryOrderFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completedOrder(@RequestBody UUID orderId);
}
