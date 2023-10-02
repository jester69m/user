package com.task.user.payloads;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class UserUpdateRequest {

    @Email
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if(email != null) {
            sb.append("\"email\":\"");
            sb.append(email);
            sb.append("\",");
        }
        if(firstName != null) {
            sb.append("\"firstName\":\"");
            sb.append(firstName);
            sb.append("\",");
        }
        if(lastName != null) {
            sb.append("\"lastName\":\"");
            sb.append(lastName);
            sb.append("\",");
        }
        if(dateOfBirth != null) {
            sb.append("\"dateOfBirth\":\"");
            sb.append(dateOfBirth);
            sb.append("\",");
        }
        if(address != null) {
            sb.append("\"address\":\"");
            sb.append(address);
            sb.append("\",");
        }
        if(phoneNumber != null) {
            sb.append("\"phoneNumber\":\"");
            sb.append(phoneNumber);
            sb.append("\",");
        }
        sb.append("}");
        return sb.toString();
    }

}
