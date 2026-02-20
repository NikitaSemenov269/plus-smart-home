package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.enums.order.OrderState;

import java.util.Optional;

public interface WarehouseService {

    void addNewProductToTheWarehouse(NewProductInWarehouseRequest newProduct);

    BookedProductsDto checkQuantityOfGoodsInStock(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    void updateProductQuantity(AddProductToWarehouseRequest request);

    void updateProductQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest,
                               Optional<OrderState> state);
}
