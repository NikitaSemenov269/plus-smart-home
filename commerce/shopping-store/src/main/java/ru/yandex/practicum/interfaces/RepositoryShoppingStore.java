package ru.yandex.practicum.interfaces;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.enums.shoppingStore.QuantityState;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface RepositoryShoppingStore extends JpaRepository<Product, UUID> {

    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.productState = 'DEACTIVATE' WHERE p.productId = :productId")
    void deactivateProduct(@Param("productId") UUID productId);
}
