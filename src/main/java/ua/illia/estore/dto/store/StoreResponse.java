package ua.illia.estore.dto.store;

import lombok.Data;

import java.util.List;

@Data
public class StoreResponse {

    private long id;

    private String uid;

    private String name;

    private List<Long> warehouses;
}
