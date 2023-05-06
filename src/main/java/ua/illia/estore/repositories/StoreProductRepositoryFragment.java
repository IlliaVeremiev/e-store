package ua.illia.estore.repositories;

import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.storeproduct.StoreProductRepositoryResponse;
import ua.illia.estore.dto.storeproduct.StoreProductSearchForm;
import ua.illia.estore.model.product.Product;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.utils.CountingPage;

import java.math.BigDecimal;
import java.util.Map;

public interface StoreProductRepositoryFragment {

    CountingPage<StoreProductRepositoryResponse> search(Store store, StoreProductSearchForm form, Map<String, Object> classification, Pageable pageable);

    BigDecimal countProductsAvailableInStore(Store store, Product product);
}
