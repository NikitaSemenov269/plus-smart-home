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
import ru.yandex.practicum.model.Dimension;
import ru.yandex.practicum.model.ProductOfWarehouse;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

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
    @Transactional
    public BookedProductsDto checkQuantityOfGoodsInStock(ShoppingCartDto shoppingCartDto) {
        if (shoppingCartDto.getShoppingCartId() == null || shoppingCartDto.getProducts().isEmpty()) {
            throw new IllegalArgumentException("");
        }

        Map<UUID, Integer> requestedProducts = shoppingCartDto.getProducts();
        List<ProductOfWarehouse> allProducts = repository.findAllById(shoppingCartDto.getProducts().keySet());

        if (requestedProducts.size() > allProducts.size()) {
            // throw new ...
        }

        List<ProductOfWarehouse> verifiedProducts = new ArrayList<>();
        List<ProductOfWarehouse> unverifiedProducts = new ArrayList<>();

        allProducts.forEach(
                product -> {
                    if (product.getQuantity() >= requestedProducts.get(product.getProductId())) {
                        verifiedProducts.add(product);
                    } else {
                        unverifiedProducts.add(product);
                    }
                });

        if (!unverifiedProducts.isEmpty()) {
            //  throw new ProductInShoppingCartLowQuantityInWarehouse(unverifiedProducts);
        }

        return BookedProductsDto.builder()
                // Суммарная масса
                .deliveryWeight(verifiedProducts.stream()
                        .mapToDouble(ProductOfWarehouse::getWeight)
                        .sum()
                )
                // Суммарный объем.
                .deliveryVolume(verifiedProducts.stream()
                        .mapToDouble(product -> {
                            return product.getDimension().getDepth() *
                                    product.getDimension().getHeight() *
                                    product.getDimension().getWidth();
                        }).sum()
                )
                // Хрупкость
                .fragile(verifiedProducts.stream()
                        .anyMatch(ProductOfWarehouse::getFragile))
                .build();
    }


    private Double calculateVolume(Dimension dimension) {
        return dimension.getWidth() * dimension.getHeight() * dimension.getDepth();
    }

    @Transactional(readOnly = true)
    private void validIdProduct(UUID idProduct) {
        if (!repository.existsById(idProduct)) {
            throw new NotFoundException("Продукт не найден на складе.");
        }
    }
}
