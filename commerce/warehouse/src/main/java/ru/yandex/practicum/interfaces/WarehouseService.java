package ru.yandex.practicum.interfaces;


import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addNewProductToTheWarehouse(NewProductInWarehouseRequest newProduct);

    BookedProductsDto checkQuantityOfGoodsInStock(ShoppingCartDto shoppingCartDto);
}
