package ua.illia.estore.services.warehouse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.product.ProductSearchForm;
import ua.illia.estore.dto.warehouse.WarehouseCreateForm;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.warehouse.Warehouse;
import ua.illia.estore.model.warehouse.WarehouseProduct;

import java.math.BigDecimal;
import java.util.List;

public interface WarehouseService {

    Warehouse create(WarehouseCreateForm warehouseDto);

    WarehouseProduct create(Warehouse warehouse, Product product, int count);

    List<Warehouse> getAll();

    List<Warehouse> getByIds(List<Long> ids);

    Warehouse getById(long id);

    Page<WarehouseProduct> getProducts(long warehouseId, ProductSearchForm form, Pageable pageable);

    WarehouseProduct addProduct(Warehouse warehouse, Product product, BigDecimal count);

    List<WarehouseProduct> getByProduct(Product product);

    List<WarehouseProduct> getAvailableProductsByWarehouseAndProduct(List<Warehouse> warehouses, Product product);

    Page<Warehouse> search(Pageable pageable);
}
