package ua.illia.estore.dto.warehouse;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WarehouseCreateForm {

    @NotEmpty
    private String name;

    private String address;
}
