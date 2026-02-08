package ru.yandex.practicum.DTO.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull
    private Double deliveryWeight; // Общий вес доставки.
    @NotNull
    private Double deliveryVolume; // Общие объём доставки.
    @NotNull
    private Boolean fragile; // Есть ли хрупкие вещи в доставке.

}
