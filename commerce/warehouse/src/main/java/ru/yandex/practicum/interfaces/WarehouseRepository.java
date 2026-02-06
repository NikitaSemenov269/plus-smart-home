package ru.yandex.practicum.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.ProductOfWarehouse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<ProductOfWarehouse, UUID> {

}
