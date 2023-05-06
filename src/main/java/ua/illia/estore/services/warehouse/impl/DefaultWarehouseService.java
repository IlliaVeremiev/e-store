package ua.illia.estore.services.warehouse.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.dto.product.ProductSearchForm;
import ua.illia.estore.dto.warehouse.WarehouseCreateForm;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.warehouse.Warehouse;
import ua.illia.estore.model.warehouse.WarehouseProduct;
import ua.illia.estore.repositories.WarehouseProductRepository;
import ua.illia.estore.repositories.WarehouseRepository;
import ua.illia.estore.search.SpecificationList;
import ua.illia.estore.services.warehouse.WarehouseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DefaultWarehouseService implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private WarehouseProductRepository warehouseProductRepository;

    @Override
    public Warehouse create(WarehouseCreateForm warehouseDto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseDto.getName());
        warehouse.setAddress(warehouseDto.getAddress());
        warehouse.setUid(String.valueOf(UUID.randomUUID()));
        return warehouseRepository.save(warehouse);
    }

    @Override
    public WarehouseProduct create(Warehouse warehouse, Product product, int count) {
        WarehouseProduct wp = new WarehouseProduct();
        wp.setWarehouse(warehouse);
        wp.setProduct(product);
        wp.setCount(BigDecimal.valueOf(count));
        return warehouseProductRepository.save(wp);
    }

    @Override
    public List<Warehouse> getAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public List<Warehouse> getByIds(List<Long> ids) {
        return warehouseRepository.findAllByIdIn(ids);
    }

    @Override
    public Warehouse getById(long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Warehouse with id: " + id + " not found", "warehouse.id"));
    }

    @Override
    public Page<WarehouseProduct> getProducts(long warehouseId, ProductSearchForm form, Pageable pageable) {
        Warehouse warehouse = getById(warehouseId);
        SpecificationList<WarehouseProduct> specification = new SpecificationList<>();
        specification.add((r, q, c) -> c.equal(r.get("warehouse"), warehouse));
        return warehouseProductRepository.findAll(specification, pageable);
    }

    @Override
    public WarehouseProduct addProduct(Warehouse warehouse, Product product, BigDecimal count) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findByWarehouseAndProduct(warehouse, product).orElseGet(() -> {
            WarehouseProduct wp = new WarehouseProduct();
            wp.setWarehouse(warehouse);
            wp.setProduct(product);
            wp.setCount(new BigDecimal(0));
            return wp;
        });

        warehouseProduct.setCount(warehouseProduct.getCount().add(count));
        return warehouseProductRepository.save(warehouseProduct);
    }

    @Override
    public List<WarehouseProduct> getByProduct(Product product) {
        return warehouseProductRepository.findAllByProduct(product);
    }

    @Override
    public List<WarehouseProduct> getAvailableProductsByWarehouseAndProduct(List<Warehouse> warehouses, Product product) {
        return warehouseProductRepository.findAllByWarehouseInAndProductAndCountGreaterThan(warehouses, product, BigDecimal.ZERO);
    }

    @Override
    public Page<Warehouse> search(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }
}
