package ru.yandex.practicum.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;
import ru.yandex.practicum.DTO.shoppingStore.ProductDto;
import ru.yandex.practicum.DTO.shoppingStore.SetProductQuantity;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ShoppingStoreService {

    Page<ProductDto> findAllByProductCategory(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean deleteProductFromAssortment(UUID productId);

    Boolean settingTheStatus(SetProductQuantity setProductQuantity);

    ProductDto getProductById(UUID productId);

    List<ProductDto> getProductsByIds(Set<UUID> productsId);;
}
