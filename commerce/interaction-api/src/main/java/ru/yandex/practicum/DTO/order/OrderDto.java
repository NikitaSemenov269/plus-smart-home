package ru.yandex.practicum.DTO.order;

import lombok.*;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID orderId;

    private ShoppingCartDto shoppingCartDto;

    private UUID paymentId;

    private UUID deliveryId;

    @Builder.Default
    private OrderState state = OrderState.NEW;
    // заменить на DTO
    private Double deliveryWeight;

    private Double deliveryVolume;

    @Builder.Default
    private Boolean fragile = true;
    // заменить на DTO
    private BigDecimal totalPrice;

    private BigDecimal deliveryPrice;

    private BigDecimal productPrice;
}
