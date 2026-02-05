package ru.yandex.practicum.interfaces;


import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addNewProductToTheWarehouse(NewProductInWarehouseRequest newProduct);
}
