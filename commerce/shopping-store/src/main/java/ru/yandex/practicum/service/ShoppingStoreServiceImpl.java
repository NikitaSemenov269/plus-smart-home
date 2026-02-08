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
import ru.yandex.practicum.interfaces.RepositoryShoppingStore;
import ru.yandex.practicum.interfaces.ShoppingStoreService;
import ru.yandex.practicum.mapper.ShoppingStoreMapper;
import ru.yandex.practicum.model.ProductOfStore;

import java.util.UUID;

// ДОРАБОТАТЬ ИСКЛЮЧЕНИЕ

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final RepositoryShoppingStore repository;
    private final ShoppingStoreMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> findAllByProductCategory(ProductCategory category, Pageable pageable) {
        try {
            log.info("");
            return repository.findAllByProductCategory(category, pageable).map(mapper::toDto);
        } catch (RuntimeException e) {
            log.error("");
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        // Если у нового объекта уже есть ID - отмена создания данного объекта.
        if (dto.getProductId() != null) {
            throw new RuntimeException();
        }
        try {
            ProductOfStore newProduct = repository.save(mapper.toProduct(dto));
            log.info("Товар успешно добавлен. Товару присвоен ID: {}", newProduct.getProductId());
            return mapper.toDto(newProduct);
        } catch (RuntimeException e) {
            log.error("В процессе добавления нового товара возникла ошибка.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        ProductOfStore product = repository.findById(productDto.getProductId()).orElseThrow(NotFoundException::new);
        mapper.updateProduct(productDto, product);
        log.info("Продукт с ID: {} успешно обновлен.", productDto.getProductId());
        return mapper.toDto(product);
    }

    @Override
    @Transactional
    public boolean deleteProductFromAssortment(UUID productId) {
        // Отсутствие товара в БД не ведет к 404 и считается корректным случаем удаления товара.
        try {
            if (!repository.existsById(productId)) {
                log.info("Продукт с ID: {} не найден в базе данных.", productId);
                return true;
            }
            repository.deactivateProduct(productId);
            log.info("Продукт с ID: {} успешно удален из ассортимента.", productId);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean SettingTheStatus(SetProductQuantity setProductQuantity) {
        if (!repository.existsById(setProductQuantity.getProductId())) {
            log.info("");
            throw new NotFoundException();
        }
        try {
            repository.settingTheStatus(setProductQuantity.getProductId(), setProductQuantity.getQuantityState());
            log.info("");
            return true;
        } catch (RuntimeException e) {
            log.error("");
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        ProductOfStore product = repository.findById(productId).orElseThrow(NotFoundException::new);
        return mapper.toDto(product);
    }
}
