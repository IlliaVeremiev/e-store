package ua.illia.estore.services.store.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.illia.estore.dto.storeproduct.StoreProductRepositoryResponse;
import ua.illia.estore.dto.storeproduct.StoreProductSearchForm;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.repositories.ProductRepository;
import ua.illia.estore.services.product.CategoryService;
import ua.illia.estore.services.product.ProductService;
import ua.illia.estore.services.store.StoreProductService;
import ua.illia.estore.services.store.StoreService;
import ua.illia.estore.utils.CountingPage;

import java.util.Map;

@Service
public class DefaultStoreProductService implements StoreProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CategoryService categoryService;


    @Override
    public StoreProductRepositoryResponse getById(String storeUid, Long productId) {
        Store store = storeService.getByUid(storeUid);
        Product product = productService.getById(productId);
        StoreProductRepositoryResponse response = new StoreProductRepositoryResponse();
        response.setProduct(product);
        response.setCount(productRepository.countProductsAvailableInStore(store, product));
        return response;
    }

    @Override
    public CountingPage<StoreProductRepositoryResponse> search(String storeUid, StoreProductSearchForm form, Map<String, Object> classification, Pageable pageable) {
        Store store = storeService.getByUid(storeUid);
        return productRepository.search(store, form, classification, pageable);
    }
}
