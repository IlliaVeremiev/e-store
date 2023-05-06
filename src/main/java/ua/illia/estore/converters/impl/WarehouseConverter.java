package ua.illia.estore.converters.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.dto.warehouse.WarehouseResponse;
import ua.illia.estore.model.warehouse.Warehouse;

@Component
public class WarehouseConverter {
    public WarehouseResponse warehouseResponse(Warehouse warehouse) {
        WarehouseResponse response = new WarehouseResponse();
        response.setId(warehouse.getId());
        response.setName(warehouse.getName());
        response.setUid(warehouse.getUid());
        response.setAddress(warehouse.getAddress());
        return response;
    }
}
