package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.ChangeQuantityOfProductToWarehouse;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.enums.order.OrderState;


@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseApi {

    @PutMapping
    void addProduct(@RequestBody NewProductInWarehouseRequest newProduct);

    @PostMapping("/check")
    BookedProductsDto checkQuantityOfGoodsInStock(@RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void increaseProductQuantity(@RequestBody ChangeQuantityOfProductToWarehouse request,
                                 @RequestParam(required = false) OrderState state);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
