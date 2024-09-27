package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CompanyDTO {

    private String id;

    private String name;

    private String address;

    private String city;

    private String phone;

    private String email;

    private String urlLogo;

    private LocalDate creationDate;

    private String LicenseType;

    private LocalDate expirationDate;

    private String Status;

    public CompanyDTO(Company company) {
        this.id = String.valueOf(company.getId());
        this.name = company.getCompanyName();
        this.address = company.getAddress();
        this.city = company.getCity();
        this.phone = company.getPhone();
        this.email = company.getCompanyEmail();
        this.urlLogo = (company.getCompanyLogo() != null) ? company.getCompanyLogo() : "https://default.url/logo.png";
        this.creationDate = company.getCreationDate();
        this.LicenseType = company.getLicense().getLicenseType().toString();
        this.expirationDate = company.getLicense().getExpirationDate();
        this.Status = company.isActive() ? "Active" : "Inactive";
    }
}
