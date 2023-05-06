package ua.illia.estore.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerResponse {

    private long id;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    private boolean locked;

    private String lockReason;

    private boolean enabled;
}
