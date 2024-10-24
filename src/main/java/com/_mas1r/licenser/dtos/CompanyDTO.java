package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CompanyDTO {

    private String id;

    private String companyName;

    private String userLogged;

    private String companyAddress;

    private String companyCity;

    private String companyPhone;

    private String companyEmail;

    private String urlLogo;

    private String primaryColor;

    private String secondaryColor;

    private String LicenseType;

    private LocalDate expirationDate;

    private String Status;

    private List<ProjectExtractDTO> projects;

    private String CompanyKeyId;

    private String whatsappToken;

    private String host;

    private int port;

    private String username;

    private String password;

    public CompanyDTO(Company company) {
        this.id = String.valueOf(company.getId());
        this.companyName = company.getCompanyName();
        this.companyAddress = company.getAddress();
        this.companyCity = company.getCity();
        this.companyPhone = company.getPhone();
        this.companyEmail = company.getCompanyEmail();
        this.urlLogo = (company.getCompanyLogo() != null) ? company.getCompanyLogo() : "https://default.url/logo.png";
        this.LicenseType = company.getLicense().getLicenseType().toString();
        this.expirationDate = company.getLicense().getExpirationDate();
        this.Status = company.isActive() ? "Active" : "Inactive";
        this.projects = company.getProjects().stream().map(ProjectExtractDTO::new).toList();
        this.CompanyKeyId = company.getCompanyKeyId();
        this.whatsappToken = company.getWhatsappToken();
        this.host = company.getSmtp().getHost();
        this.port = company.getSmtp().getPort();
        this.username = company.getSmtp().getUsername();
        this.password = company.getSmtp().getPassword();
    }
}
