package ua.illia.estore.dto.warehouse;

import lombok.Data;

@Data
public class WarehouseResponse {

    private long id;

    private String name;

    private String uid;

    private String address;
}
