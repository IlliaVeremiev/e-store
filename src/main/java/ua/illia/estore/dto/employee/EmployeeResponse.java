package ua.illia.estore.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ua.illia.estore.dto.paymentaccount.PaymentAccountResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class EmployeeResponse {
    private long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String displayName;

    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    private String email;

    private String phoneNumber;

    private List<GrantedAuthority> authorities;

    private Map<String, String> storeAuthorities;

    private List<PaymentAccountResponse> paymentAccounts;
}
