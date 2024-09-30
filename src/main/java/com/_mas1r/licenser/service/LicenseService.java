package com._mas1r.licenser.service;

import com._mas1r.licenser.models.License;
import com._mas1r.licenser.models.LicenseType;
import com._mas1r.licenser.models.Project;
import jakarta.mail.MessagingException;

import java.time.LocalDate;
import java.util.UUID;

public interface LicenseService {
    LocalDate licenseExpiration(LicenseType licenseType, LocalDate creationDate);

    License createLicense(LicenseType licenseType, LocalDate creationDate, Project project) throws MessagingException;

    License renewLicense(UUID projectId) throws MessagingException;

    boolean checkLicense(String url);
}
