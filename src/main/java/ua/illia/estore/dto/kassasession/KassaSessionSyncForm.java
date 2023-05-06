package ua.illia.estore.dto.kassasession;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.illia.estore.model.store.enums.KassaSessionState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class KassaSessionSyncForm {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime openDateTime;

    private BigDecimal openCacheAmount;

    private long employee;

    private long store;

    private KassaSessionState state;

    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime closeDateTime;

    private BigDecimal closeCacheAmount;
}
