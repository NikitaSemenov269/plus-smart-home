package ru.yandex.practicum.DTO.order;

import jakarta.validation.Valid;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private UUID orderId;

    @Valid
    private ShoppingCartDto shoppingCartDto;

    private UUID paymentId;

    private UUID deliveryId;

    @Builder.Default
    private OrderState state = OrderState.NEW;

    private BookedProductsDto bookedProductsDto;

    private BigDecimal totalPrice;

    private BigDecimal deliveryPrice;

    private BigDecimal productPrice;
}
