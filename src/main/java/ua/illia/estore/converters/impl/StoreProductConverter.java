package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.storeproduct.StoreProductRepositoryResponse;
import ua.illia.estore.dto.storeproduct.StoreProductResponse;
import ua.illia.estore.repositories.ProductVariantItemRepository;
import ua.illia.estore.services.store.StoreProductService;

import java.math.BigDecimal;

@Component
public class StoreProductConverter {

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private ProductVariantItemRepository productVariantItemRepository;

    public StoreProductResponse storeProductResponse(StoreProductRepositoryResponse product) {
        StoreProductResponse response = productConverter.convert(product.getProduct(), new StoreProductResponse());
        response.setAvailable(product.getCount().compareTo(BigDecimal.ZERO) > 0);
        response.setPrice(product.getProduct().getPrice());
        return response;
    }
}
