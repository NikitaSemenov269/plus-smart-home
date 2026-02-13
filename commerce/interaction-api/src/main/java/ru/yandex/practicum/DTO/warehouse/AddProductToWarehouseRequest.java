package ru.yandex.practicum.DTO.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToWarehouseRequest {
    @NotNull(message = "ID продукта обязательное поле.")
    private UUID productId;

    @NotNull(message = "Количество не может равняться null")
    @Positive(message = "Количество не может быть отрицательным числом.")
    private Long quantity;
}
