package ru.yandex.practicum.DTO.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.delivery.DeliveryState;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private AddressDto addressOfWarehouseDto;

    @NotNull
    private AddressDto addressOfClientDto;

    @NotNull
    private BookedProductsDto bookedProductsDto;

    @Builder.Default
    private DeliveryState deliveryState = DeliveryState.CREATED;
}
