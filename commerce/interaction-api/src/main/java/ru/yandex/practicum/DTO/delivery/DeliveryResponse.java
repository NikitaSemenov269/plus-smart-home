package ru.yandex.practicum.DTO.delivery;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.enums.delivery.DeliveryState;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    @NotNull
    private UUID deliveryId;

    @NotNull
    private UUID orderId;
    // а нужны ли адреса?
    @NotNull
    private AddressDto addressOfWarehouseDto;

    @NotNull
    private AddressDto addressOfClientDto;

    @Min(0)
    private BigDecimal shippingCost;

    private DeliveryState deliveryState;
}
