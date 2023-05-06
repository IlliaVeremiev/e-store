package ua.illia.estore.dto.customer;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class CustomerOauthCreateForm {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String uid;

    private String firstName;

    private String lastName;

    private String locale;

    private String picture;
}
