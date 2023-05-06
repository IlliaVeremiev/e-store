package ua.illia.estore.dto.store;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class StoreCreateForm {

    @NotEmpty
    private String name;

    @NotEmpty
    private String uid;

    private List<Long> warehouses;
}
