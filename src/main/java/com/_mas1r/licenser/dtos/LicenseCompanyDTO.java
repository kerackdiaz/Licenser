package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.License;

import java.time.LocalDate;

public class LicenseCompanyDTO {

    private String licenseType;

    private LocalDate creationDate;

    private LocalDate expirationDate;

    public LicenseCompanyDTO(License license) {
        this.licenseType = license.getLicenseType().toString();
        this.creationDate = license.getCreationDate();
        this.expirationDate = license.getExpirationDate();
    }
}
