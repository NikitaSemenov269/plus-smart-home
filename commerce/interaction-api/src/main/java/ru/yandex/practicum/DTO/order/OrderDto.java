package ru.yandex.practicum.DTO.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID orderId;
    private UUID shoppingCartId;
    // products; Объект
    private UUID paymentId;
    private UUID deliveryId;
    //    state; Enum
    private Double deliveryWeight;
    private Double deliveryVolume;
    @Builder.Default
    private Boolean fragile = true;
    private BigDecimal totalPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal productPrice;
}
