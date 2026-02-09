package ru.yandex.practicum.DTO.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {

    @NotNull(message = "ID продукта обязательное поле.")
    private UUID productId;

    @Builder.Default
    private Boolean fragile = true;

    @NotNull(message = "Описание товара обязательно.")
    @Valid
    private DimensionDto dimension;

    @Min(value = 1, message = "Вес товара не может быть меньше 1.")
    @Builder.Default
    private Double weight = 1.0;

}
