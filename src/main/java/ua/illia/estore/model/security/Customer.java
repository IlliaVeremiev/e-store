package ua.illia.estore.model.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_pk_sequence_generator")
    @SequenceGenerator(name = "customers_pk_sequence_generator", sequenceName = "customers_pk_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @JsonIgnore
    private String password;

    private String firstName;

    private String lastName;

    private String patronymic;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    private boolean locked;

    private String lockReason;

    private boolean enabled;

    @Column(unique = true)
    private String uid;

    private String authorizationToken;
}
