package com.task.user.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "First name required")
    private String firstName;
    @NotBlank(message = "Last name required")
    private String lastName;
    @Past(message = " Value must be earlier than current date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;

}
