package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.License;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LicenseStatus {

    private String licenseStatus;

    private LocalDate expirationDate;

    private String urlRedirect;

    public LicenseStatus(License license) {
        this.licenseStatus = license.getProject().isStatusLicense() ? "Active" : "Inactive";
        this.expirationDate = license.getExpirationDate();
        this.urlRedirect = license.getProject().getRedirect();
    }
}
