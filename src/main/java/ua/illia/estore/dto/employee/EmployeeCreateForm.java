package ua.illia.estore.dto.employee;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateForm {

    private String firstName;

    private String lastName;

    private String patronymic;

    private LocalDate dateOfBirth;

    private String email;

    private String phoneNumber;
}
