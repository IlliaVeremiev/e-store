package ua.illia.estore.dto.kassasession;

import lombok.Data;
import ua.illia.estore.model.store.enums.KassaSessionState;

@Data
public class KassaSessionSearchForm {

    private String email;

    private KassaSessionState state;
}
