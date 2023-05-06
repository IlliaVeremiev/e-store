package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.dto.store.StoreResponse;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.model.warehouse.Warehouse;

import java.util.stream.Collectors;

@Component
public class StoreConverter {

    @Autowired
    private WarehouseConverter warehouseConverter;

    public StoreResponse storeResponse(Store store) {
        StoreResponse response = new StoreResponse();
        response.setId(store.getId());
        response.setUid(store.getUid());
        response.setName(store.getName());
        response.setWarehouses(store.getWarehouses().stream().map(Warehouse::getId).collect(Collectors.toList()));
        return response;
    }
}
