package com._mas1r.licenser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyDTO {
    private String companyName;
    private String companyEmail;
    private String companyLogo;
    private String adminFirstName;
    private String adminLastName;
    private String adminUserName;
    private String adminEmail;
    private String adminPassword;

}
