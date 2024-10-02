package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CompanyExtractDTO {

    private String id;

    private String name;

    private String phone;

    private String email;

    private String LicenseType;

    private LocalDate expirationDate;

    private String Status;


    public CompanyExtractDTO(Company company) {
        this.id = company.getId().toString();
        this.name = company.getCompanyName();
        this.phone = company.getPhone();
        this.email = company.getCompanyEmail();
        this.LicenseType = company.getLicense().getLicenseType().toString();
        this.expirationDate = company.getLicense().getExpirationDate();
        this.Status = company.isActive() ? "Active" : "Inactive";
    }
}
