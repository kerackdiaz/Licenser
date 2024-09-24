package com._mas1r.licenser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String userEmail;
    private String password;
    private String id;
}