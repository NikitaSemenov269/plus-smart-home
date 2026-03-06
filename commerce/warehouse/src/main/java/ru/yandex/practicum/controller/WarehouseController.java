package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.ChangeQuantityOfProductToWarehouse;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.api.WarehouseApi;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.interfaces.WarehouseService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseApi {
    private final WarehouseService service;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addProduct(@Valid @RequestBody NewProductInWarehouseRequest newProduct) {
        log.info("PUT. Добавление продукта на склад");
        service.addNewProductToTheWarehouse(newProduct);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkQuantityOfGoodsInStock(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("");
        return service.checkQuantityOfGoodsInStock(shoppingCartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void increaseProductQuantity(@Valid @RequestBody ChangeQuantityOfProductToWarehouse request,
                                        @RequestParam(required = false) OrderState state) {
        log.debug("Пополнение запасов: {}", request);
        service.updateProductQuantity(request, state);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getWarehouseAddress() {
        log.info("Запрос адреса склада");
        return service.getWarehouseAddress();
    }
}
