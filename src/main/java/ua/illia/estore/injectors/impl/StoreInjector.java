package ua.illia.estore.injectors.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.injectors.EntityDataInjector;
import ua.illia.estore.model.store.Store;
import ua.illia.estore.services.warehouse.WarehouseService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreInjector extends EntityDataInjector<Store> {

    @Autowired
    private WarehouseService warehouseService;

    public StoreInjector() {
        field("uid", Store::setUid);
        field("name", Store::setName);
        field("warehouses", this::setWarehouses);
    }

    private void setWarehouses(Store store, List<Object> warehousesIds) {
        if (warehousesIds != null) {
            store.setWarehouses(warehouseService.getByIds(warehousesIds.stream()
                    .map(Object::toString)
                    .map(Long::valueOf)
                    .collect(Collectors.toList())));
        }
    }
}
