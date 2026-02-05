package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interfaces.WarehouseRepository;
import ru.yandex.practicum.interfaces.WarehouseService;
import ru.yandex.practicum.mapper.WarehouseMapper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    @Override
    @Transactional
    public void addNewProductToTheWarehouse(NewProductInWarehouseRequest newProduct) {
        if (repository.existsById(newProduct.getProductId())) {
            //  throw new BadAttributeValueExpException("Продукт уже имеется на складе."); кастомное искл.
        }
        try {
            repository.save(mapper.toEntity(newProduct));
            log.info("");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookedProductsDto checkQuantityOfGoodsInStock(ShoppingCartDto shoppingCartDto) {

        return null;
    }


    @Transactional(readOnly = true)
    private void validIdProduct(UUID idProduct) {
        if (!repository.existsById(idProduct)) {
            throw new NotFoundException("Продукт не найден на складе.");
        }
    }
}
