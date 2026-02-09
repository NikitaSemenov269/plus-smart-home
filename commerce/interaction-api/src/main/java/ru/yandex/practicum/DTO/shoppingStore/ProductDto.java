package ru.yandex.practicum.DTO.shoppingStore;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.enums.shoppingStore.ProductState;
import ru.yandex.practicum.enums.shoppingStore.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID productId;

    @NotBlank(message = "Название товара не может быть пустой строкой.")
    private String productName;

    @NotBlank(message = "Описание товара обязательно.")
    private String description;

    private String imageSrc;

    @NotNull(message = "Значение остатка обязательно.")
    private QuantityState quantityState;

    @NotNull(message = "Значение статуса корзины обязательно.")
    private ProductState productState;

    private ProductCategory productCategory;

    @Min(value = 1, message = "Минимальная цена должна равняться 1.")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;
}
