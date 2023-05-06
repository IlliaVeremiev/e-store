package ua.illia.estore.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.illia.estore.converters.impl.StoreProductConverter;
import ua.illia.estore.dto.storeproduct.StoreProductResponse;
import ua.illia.estore.dto.storeproduct.StoreProductSearchForm;
import ua.illia.estore.services.store.StoreProductService;
import ua.illia.estore.utils.CountingPage;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/public/api/v1/products")
public class StoreProductController {

    public static final String ONLINE_STORE_ID = "site";

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private StoreProductConverter storeProductConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @GetMapping
    public CountingPage<StoreProductResponse> search(StoreProductSearchForm form,
                                                     @PageableDefault(size = 48) Pageable pageable,
                                                     @RequestParam(required = false) String classification) throws JsonProcessingException {
        Map<String, Object> classificationMap = classification != null ? objectMapper.readValue(classification, Map.class) : Collections.emptyMap();
        return storeProductService.search(ONLINE_STORE_ID, form, classificationMap, pageable)
                .map(storeProductConverter::storeProductResponse);
    }

    @Transactional
    @GetMapping("/{productId}")
    public StoreProductResponse getById(@PathVariable long productId) {
        return storeProductConverter.storeProductResponse(storeProductService.getById(ONLINE_STORE_ID, productId));
    }
}
