package ua.illia.estore.dto.kassasession;

import lombok.Data;

@Data
public class KassaSessionCreateForm {

    private String email;

    private long storeId;
}
