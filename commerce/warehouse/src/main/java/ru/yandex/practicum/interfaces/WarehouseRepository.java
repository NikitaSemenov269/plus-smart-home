package ru.yandex.practicum.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.ProductOfWarehouse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<ProductOfWarehouse, UUID> {

    @Query("SELECT CASE WHEN COUNT(p) = :size THEN true ELSE false END " +
            "FROM ProductOfWarehouse p " +
            "WHERE p.productId IN :ids")
    boolean allProductsExist(@Param("ids") Set<UUID> ids, @Param("size") long size);

    @Query("SELECT p.productId FROM ProductOfWarehouse p WHERE p.productId IN :ids")
    List<UUID> findExistingIds(@Param("ids") Set<UUID> ids);
}
