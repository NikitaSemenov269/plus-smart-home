package ru.yandex.practicum.interfaces;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.enums.shoppingStore.ProductCategory;
import ru.yandex.practicum.enums.shoppingStore.QuantityState;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface RepositoryShoppingStore extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.productCategory = :category AND p.productState = 'ACTIVE'")
    Page<Product> findAllByProductCategory(@Param("category") ProductCategory category, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.productState = 'DEACTIVATE' WHERE p.productId = :productId")
    void deactivateProduct(@Param("productId") UUID productId);

    @Modifying
    @Query("UPDATE Product p SET p.quantityState = :quantityState WHERE p.productId = :productId")
    void settingTheStatus(@Param("productId") UUID productId,
                          @Param("quantityState") QuantityState quantityState);
}
