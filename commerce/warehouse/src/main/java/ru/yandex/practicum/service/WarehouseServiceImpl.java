package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.DTO.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.interfaces.WarehouseRepository;
import ru.yandex.practicum.interfaces.WarehouseService;
import ru.yandex.practicum.mapper.WarehouseMapper;
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
            throw new SpecifiedProductAlreadyInWarehouseException("Продукт уже имеется на складе.");
        }
        repository.save(mapper.toEntity(newProduct));
        log.info("Продукт с ID: {} успешно добавлен на склад.", newProduct.getProductId());
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkQuantityOfGoodsInStock(ShoppingCartDto shoppingCartDto) {
        log.info("Начало проверки достаточности товаров на складе.");
        if (shoppingCartDto.getShoppingCartId() == null || shoppingCartDto.getProducts().isEmpty()) {
            log.info("Для проверки была передана пустая корзина.");
            return emptyCart();
        }

        Map<UUID, Integer> requestedProducts = shoppingCartDto.getProducts();
        List<ProductOfWarehouse> products = repository.findAllById(requestedProducts.keySet());

        if (products.size() < requestedProducts.size()) {
            Set<UUID> foundIds = products.stream()
                    .map(ProductOfWarehouse::getProductId)
                    .collect(Collectors.toSet());
            Set<UUID> missingIds = requestedProducts.keySet().stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new NoSpecifiedProductInWarehouseException("Товары не найдены на складе: " + missingIds);
        }

        Map<UUID, Integer> insufficientProducts = new HashMap<>();
        List<ProductOfWarehouse> verifiedProducts = new ArrayList<>();

        for (ProductOfWarehouse product : products) {
            Integer requestedQty = requestedProducts.get(product.getProductId());

            if (product.getQuantity() >= requestedQty) {
                verifiedProducts.add(product);
            } else {
                insufficientProducts.put(
                        product.getProductId(),
                        (int) (requestedQty - product.getQuantity())
                );
            }
        }

        if (!insufficientProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    "Недостаточно товаров: " + insufficientProducts
            );
        }

        return BookedProductsDto.builder()
                // Суммарная масса
                .deliveryWeight(verifiedProducts.stream()
                        .mapToDouble(ProductOfWarehouse::getWeight)
                        .sum())
                // Суммарный объем.
                .deliveryVolume(verifiedProducts.stream()
                        .mapToDouble(product ->
                                product.getDimension().getWidth() *
                                        product.getDimension().getHeight() *
                                        product.getDimension().getDepth())
                        .sum())
                // Хрупкость
                .fragile(verifiedProducts.stream()
                        .anyMatch(ProductOfWarehouse::getFragile))
                .build();
    }


    @Override
    public void updateProductQuantity(AddProductToWarehouseRequest request) {
        updateProductQuantity(request, Optional.empty());
    }

    // Перегрузка метода
    @Override
    @Transactional
    public void updateProductQuantity(AddProductToWarehouseRequest request, Optional<OrderState> state) {
        log.debug("Изменение остатков продуктов {} на складе.", request.toString());
        Set<UUID> productIds = request.getProducts().keySet();

        validIdsProduct(productIds);

        List<ProductOfWarehouse> products = repository.findAllById(productIds);
        repository.saveAll(products.stream()
                .map(product -> {
                    Long quantity = request.getProducts().get(product.getProductId());
                    if (state.isPresent() && state.get() == OrderState.PRODUCT_RETURNED) {
                        product.setQuantity(product.getQuantity() + quantity);
                    } else {
                        product.setQuantity(product.getQuantity() - quantity);
                    }
                    return product;
                }).toList());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    @Transactional(readOnly = true)
    public void validIdsProduct(Set<UUID> idsProduct) {
        if (idsProduct.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Передана пустая коллекция.");
        }
        if (!repository.allProductsExist(idsProduct, idsProduct.size())) {
            List<UUID> productIds = repository.findExistingIds(idsProduct);

            Set<UUID> missingIds = idsProduct.stream()
                    .filter(id -> !productIds.contains(id))
                    .collect(Collectors.toSet());

            throw new NoSpecifiedProductInWarehouseException("Товары не найдены на складе: " + missingIds);
        }
    }

    private BookedProductsDto emptyCart() {
        return BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();
    }
}
