package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.shoppingStore.ProductDto;
import ru.yandex.practicum.DTO.shoppingStore.SetProductQuantity;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.exception.shoppingStore.ProductNotFoundException;
import ru.yandex.practicum.interfaces.RepositoryShoppingStore;
import ru.yandex.practicum.interfaces.ShoppingStoreService;
import ru.yandex.practicum.mapper.ShoppingStoreMapper;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final RepositoryShoppingStore repository;
    private final ShoppingStoreMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAllByProductCategory(ProductCategory category, Pageable pageable) {
        log.info("Начато получение всех товаров {} категории.", category);
        return repository.findAllByProductCategory(category, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        // Если у нового объекта уже есть ID - отмена создания данного объекта.
        if (dto.getProductId() != null) {
            if (repository.existsById(dto.getProductId())) {
                log.info("Выявлена попытка создания нового товара с установленным ID. Начат поиск товара в БД.");
                return mapper.toDto(repository.findById(dto.getProductId()).orElseThrow(
                        () -> new ProductNotFoundException("Товар с ID {} не найден. Создание товара с установленным ID " +
                                "не допускается.")));
            }
            throw new IllegalArgumentException("Создание товара с установленным ID " + dto.getProductId() +
                    "не допускается.");
        }
        Product newProduct = repository.save(mapper.toProduct(dto));
        log.info("Товар успешно добавлен. Товару присвоен ID: {}", newProduct.getProductId());
        return mapper.toDto(newProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = repository.findById(productDto.getProductId()).orElseThrow(NotFoundException::new);
        mapper.updateProduct(productDto, product);
        log.info("Продукт с ID: {} успешно обновлен.", productDto.getProductId());
        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public Boolean deleteProductFromAssortment(UUID productId) {
        // Отсутствие товара в БД не ведет к 404 и считается корректным случаем удаления товара.
        if (!repository.existsById(productId)) {
            log.info("Продукт с ID: {} не найден в базе данных.", productId);
            return true;
        }
        repository.deactivateProduct(productId);
        log.info("Продукт с ID: {} успешно удален из ассортимента.", productId);
        return true;
    }

    @Override
    @Transactional
    public Boolean settingTheStatus(SetProductQuantity setProductQuantity) {

        Product product = repository.findById(setProductQuantity.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("Не удалось изменить статут остатка продукта."));

        product.setQuantityState(setProductQuantity.getQuantityState());

        log.info("Статус остатка продукта ID {} успешно изменен на {}", setProductQuantity.getProductId(),
                setProductQuantity.getQuantityState());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        log.info("Начата попытка получения продукта по ID {}", productId);
        Product product = repository.findById(productId).orElseThrow(NotFoundException::new);
        return mapper.toDto(product);
    }
}
