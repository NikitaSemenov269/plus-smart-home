package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.shoppingStore.ProductDto;
import ru.yandex.practicum.DTO.shoppingStore.SetProductQuantity;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.enums.shoppingStore.QuantityState;
import ru.yandex.practicum.interfaces.ShoppingStoreService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {
    private final ShoppingStoreService service;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("PUT. Добавление продукта на витрину");
        return service.createProduct(productDto);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProductById(@PathVariable
                                     @NotNull(message = "ID продукта не может равняться null.") UUID productId) {
        log.info("GET. Получение продукта с ID: {}", productId);
        return service.getProductById(productId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProductsByCategory(
            @NotNull(message = "Категория продукта не может быть null.")
            @RequestParam ProductCategory category,
            @PageableDefault(page = 0, size = 20, sort = "productName", direction = Sort.Direction.DESC)
            Pageable pageable) {
        log.info("GET. Получение продуктов по категории: {}", category);
        return service.findAllByProductCategory(category, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("POST. Обновление продукта с ID: {}", productDto.getProductId());
        return service.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeProduct(@RequestBody
                                 @NotNull(message = "ID продукта не может равняться null.") UUID productId) {
        log.info("POST. Удаление продукта с ID: {} из общедоступного каталога.", productId);
        return service.deleteProductFromAssortment(productId);
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public boolean setQuantityState(UUID productId, QuantityState quantityState) {
        log.info("POST. Обновление у позиции: {} поля остатков продукта: {}",
                productId, quantityState);
        return service.settingTheStatus(SetProductQuantity.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build());
    }
}
