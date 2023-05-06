package ua.illia.estore.dto.customer;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class CustomerCreateForm {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
