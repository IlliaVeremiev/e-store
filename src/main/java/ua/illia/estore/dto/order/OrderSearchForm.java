package ua.illia.estore.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderSearchForm {

    private Long customerId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String phoneNumber;

    private String orderUid;
}
