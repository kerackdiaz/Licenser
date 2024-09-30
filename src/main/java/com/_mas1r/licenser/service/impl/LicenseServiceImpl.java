package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.models.License;
import com._mas1r.licenser.models.LicenseType;
import com._mas1r.licenser.models.Project;
import com._mas1r.licenser.repositories.LicenseRepository;
import com._mas1r.licenser.repositories.ProjectRepository;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.utils.SerialKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private SerialKey key;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public LocalDate licenseExpiration(LicenseType licenseType, LocalDate creationDate){
        if(licenseType.equals(LicenseType.DEMO)){
            return creationDate.plusDays(15);

        } else if (licenseType.equals(LicenseType.MENSUAL)){
            return creationDate.plusMonths(1);

        } else if (licenseType.equals(LicenseType.TRIMESTRAL)){
            return creationDate.plusMonths(3);

        } else if (licenseType.equals(LicenseType.SEMESTRAL)){
            return creationDate.plusMonths(6);

        } else if (licenseType.equals(LicenseType.ANUAL)){
            return creationDate.plusYears(1);

        } else if (licenseType.equals(LicenseType.VITALICIA)){
            return creationDate.plusYears(100);
        }
        return null;
    }

    @Override
    public License createLicense(LicenseType licenseType, LocalDate creationDate, Project project) {
        License license = new License();
        license.setLicenseType(licenseType);
        license.setCreationDate(creationDate);
        license.setExpirationDate(licenseExpiration(licenseType, creationDate));
        license.setLicenseKey(key.generateLicenseKey());
        license.setProject(project);
        licenseRepository.save(license);
        return license;
    }

    @Override
    public License renewLicense(UUID projectId) {
        Project project= projectRepository.findById(projectId).orElse(null);
        assert project != null;
        License license = licenseRepository.findById(project.getLicense().getId()).orElse(null);
        assert license != null;
        LocalDate creationDate = license.getProject().getInitDate();
        license.setCreationDate(creationDate);
        license.setExpirationDate(licenseExpiration(license.getLicenseType(), creationDate));
        licenseRepository.save(license);
        return license;
    }


    @Override
    public boolean checkLicense(String url){
        Project project = projectRepository.findByProjectUrl(url).orElse(null);
        assert project != null;
        return  project.getExpDate().isBefore(LocalDate.now());
    }

}
