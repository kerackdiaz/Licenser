package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class LicenseCompanyStatus {

    private Boolean StatusCompany;

    private Boolean licensestatus; //renovar licencia


    public LicenseCompanyStatus(Company company) {
        this.StatusCompany = company.isActive();
        this.licensestatus = company.getLicense().getExpirationDate().isAfter(LocalDate.now());
    }
}
