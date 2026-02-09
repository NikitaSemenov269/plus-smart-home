package ru.yandex.practicum.DTO.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull(message = "Вес доставки обязательный параметр.")
    private Double deliveryWeight; // Общий вес доставки.
    @NotNull(message = "Объем доставки обязательный параметр.")
    private Double deliveryVolume; // Общие объём доставки.
    @NotNull(message = "Хрупкость обязательный параметр.")
    private Boolean fragile; // Есть ли хрупкие вещи в доставке.

}
