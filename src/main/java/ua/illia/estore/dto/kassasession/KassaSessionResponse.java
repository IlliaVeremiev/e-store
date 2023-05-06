package ua.illia.estore.dto.kassasession;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.illia.estore.dto.employee.EmployeeResponse;
import ua.illia.estore.dto.store.StoreResponse;
import ua.illia.estore.model.store.enums.KassaSessionState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class KassaSessionResponse {

    private long id;

    private EmployeeResponse employee;

    private StoreResponse store;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    private KassaSessionState state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime openDateTime;

    private BigDecimal openCacheAmount;

    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime closeDateTime;

    private BigDecimal closeCacheAmount;
}
