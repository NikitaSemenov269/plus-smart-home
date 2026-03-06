package ru.yandex.practicum.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookedProducts {
    @NotNull(message = "Вес доставки обязательный параметр.")
    private Double deliveryWeight; // Общий вес доставки.
    @NotNull(message = "Объем доставки обязательный параметр.")
    private Double deliveryVolume; // Общие объём доставки.
    @NotNull(message = "Хрупкость обязательный параметр.")
    private Boolean fragile; // Есть ли хрупкие вещи в доставке.
}
