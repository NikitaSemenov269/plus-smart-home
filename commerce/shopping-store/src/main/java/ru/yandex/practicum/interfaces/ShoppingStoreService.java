package ru.yandex.practicum.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.DTO.shoppingStore.ProductDto;
import ru.yandex.practicum.DTO.shoppingStore.SetProductQuantity;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface ShoppingStoreService {

    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);

    String createProduct(ProductDto dto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProductFromAssortment(UUID productId);

    boolean SettingTheStatus(SetProductQuantity setProductQuantity);

    ProductDto getProductById(UUID productId);
}
