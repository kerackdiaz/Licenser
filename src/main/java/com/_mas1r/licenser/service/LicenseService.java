package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.License;
import com._mas1r.licenser.models.LicenseType;
import com._mas1r.licenser.models.Project;
import jakarta.mail.MessagingException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface LicenseService {
    LocalDate licenseExpiration(LicenseType licenseType, LocalDate creationDate);

    License createLicense(LicenseType licenseType, LocalDate creationDate, Project project, Company company) throws MessagingException;

    License renewLicense(Long projectId) throws MessagingException;

    Map<String,Object> checkLicense(String url, String key);

    String updateLicenseCompany(String id, LicenseCompanyStatus license);
}
