package ua.illia.estore.services.store;

import org.springframework.data.domain.Pageable;
import ua.illia.estore.dto.storeproduct.StoreProductRepositoryResponse;
import ua.illia.estore.dto.storeproduct.StoreProductSearchForm;
import ua.illia.estore.utils.CountingPage;

import java.util.Map;

public interface StoreProductService {

    StoreProductRepositoryResponse getById(String storeUid, Long productId);

    CountingPage<StoreProductRepositoryResponse> search(String storeUid, StoreProductSearchForm form, Map<String, Object> classification, Pageable pageable);
}
