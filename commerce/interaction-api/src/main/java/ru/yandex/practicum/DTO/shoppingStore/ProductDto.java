package ru.yandex.practicum.DTO.shoppingStore;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.enums.shoppingStore.ProductState;
import ru.yandex.practicum.enums.shoppingStore.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private UUID productId;

    @NotBlank()
    private String productName;

    @NotBlank()
    private String description;

    private String imageSrc;

    @NonNull
    private QuantityState quantityState;

    @NonNull
    private ProductState productState;

    private ProductCategory productCategory;

    @Min(1)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;
}
