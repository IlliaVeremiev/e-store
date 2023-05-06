package ua.illia.estore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.warehouse.Warehouse;
import ua.illia.estore.model.warehouse.WarehouseProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WarehouseProductRepository extends PagingAndSortingRepository<WarehouseProduct, Long> {

    Page<WarehouseProduct> findAll(Specification<WarehouseProduct> specification, Pageable pageable);

    Optional<WarehouseProduct> findByWarehouseAndProduct(Warehouse warehouse, Product product);

    List<WarehouseProduct> findAllByProduct(Product product);

    List<WarehouseProduct> findAllByWarehouseInAndProductAndCountGreaterThan(List<Warehouse> warehouses, Product product, BigDecimal count);
}
