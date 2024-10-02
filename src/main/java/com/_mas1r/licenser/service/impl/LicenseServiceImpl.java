package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.dtos.LicenseStatus;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.License;
import com._mas1r.licenser.models.LicenseType;
import com._mas1r.licenser.models.Project;
import com._mas1r.licenser.repositories.CompanyRepository;
import com._mas1r.licenser.repositories.LicenseRepository;
import com._mas1r.licenser.repositories.ProjectRepository;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.utils.SerialKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private SerialKey key;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyRepository companyRepository;

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
    public License createLicense(LicenseType licenseType, LocalDate creationDate, Project project,Company company) {
        License license = new License();
        license.setLicenseType(licenseType);
        license.setCreationDate(creationDate);
        license.setExpirationDate(licenseExpiration(licenseType, creationDate));
        license.setLicenseKey(key.generateLicenseKey(8));
        license.setProject(project);
        license.setCompany(company);
        licenseRepository.save(license);
        return license;
    }

    @Override
    public License renewLicense(Long projectId) {
        Project project= projectRepository.findById(projectId).orElse(null);
        assert project != null;
        License license = licenseRepository.findById(project.getLicense().getId()).orElse(null);
        assert license != null;
        LocalDate creationDate = license.getProject().getInitDate();
        license.setCreationDate(creationDate);
        license.setExpirationDate(licenseExpiration(license.getLicenseType(), creationDate));
        project.setStatusLicense(true);
        licenseRepository.save(license);
        projectRepository.save(project);
        return license;
    }


    @Override
    public Map<String,Object> checkLicense(String url, String key) {
        Map<String, Object> response = new HashMap<>();
        Project project = projectRepository.findByProjectUrl(url).orElse(null);
        if(companyRepository.existsByCompanyKeyId(key)){
        assert project != null;
        if (project.isStatusProject()) {
            LicenseStatus licenseStatus = new LicenseStatus(project.getLicense());
            response.put("License Status", licenseStatus);
            return response;
        } else {
            LicenseStatus newLicenseStatus = new LicenseStatus();
            newLicenseStatus.setLicenseStatus("Finale");
            response.put("License Status", newLicenseStatus);
            return response;
            }
        }
        response.put("Error", "Company not found");
        return response;
    }

    @Override
    public String updateLicenseCompany(String id, LicenseCompanyStatus license){
        Company company = companyRepository.findById(UUID.fromString(id)).orElse(null);
        if (company == null) {
            return "Company not found";
        }
        if(company.isActive() && license.getStatusCompany() && license.getLicensestatus()) {
            company.getLicense().setExpirationDate(licenseExpiration(company.getLicense().getLicenseType(), LocalDate.now()));
            companyRepository.save(company);
            return "Company license updated";
        } else if (!company.isActive() && license.getStatusCompany()) {
            company.getLicense().setExpirationDate(licenseExpiration(company.getLicense().getLicenseType(), LocalDate.now()));
            company.setActive(license.getStatusCompany());
            companyRepository.save(company);
            return "Company license updated and activated";
        } else if (company.isActive() && !license.getStatusCompany()) {
            company.setActive(false);
            companyRepository.save(company);
            return "Login has been deactivated for " + company.getCompanyName();
        }
        return "License company not found";
    }
}
