package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.product.WarehouseProductResponse;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.warehouse.WarehouseProduct;

@Component
public class WarehouseProductConverter {

    @Autowired
    private ProductConverter productConverter;

    public WarehouseProductResponse warehouseProductResponse(WarehouseProduct warehouseProduct) {
        WarehouseProductResponse response = new WarehouseProductResponse();
        Product product = warehouseProduct.getProduct();
        response = productConverter.convert(product, response);
        response.setCount(warehouseProduct.getCount());
        return response;
    }
}
