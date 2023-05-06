package ua.illia.estore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.converters.impl.WarehouseProductConverter;
import ua.illia.estore.dto.product.ProductSearchForm;
import ua.illia.estore.dto.product.WarehouseProductResponse;
import ua.illia.estore.services.warehouse.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouses/{warehouseId}/products")
public class WarehouseProductsController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseProductConverter warehouseProductConverter;

    @Transactional
    @GetMapping
    public Page<WarehouseProductResponse> getProductsPage(@PathVariable long warehouseId,
                                                          ProductSearchForm productSearchForm,
                                                          @PageableDefault(size = 24) Pageable pageable) {
        return warehouseService.getProducts(warehouseId, productSearchForm, pageable)
                .map(warehouseProductConverter::warehouseProductResponse);
    }
}
