package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.ChangeQuantityOfProductToWarehouse;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.enums.order.OrderState;


public interface WarehouseService {

    void addNewProductToTheWarehouse(NewProductInWarehouseRequest newProduct);

    BookedProductsDto checkQuantityOfGoodsInStock(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    void updateProductQuantity(ChangeQuantityOfProductToWarehouse addProductToWarehouseRequest,
                               OrderState state);
}
