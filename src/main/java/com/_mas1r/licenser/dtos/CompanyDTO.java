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

    private String name;

    private String address;

    private String city;

    private String phone;

    private String email;

    private String urlLogo;

    private String primaryColor;

    private String secondaryColor;

    private LocalDate creationDate;

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
        this.projects = company.getProjects().stream().map(ProjectExtractDTO::new).toList();
        this.CompanyKeyId = company.getCompanyKeyId();
        this.whatsappToken = company.getWhatsappToken();
        this.host = company.getSmtp().getHost();
        this.port = company.getSmtp().getPort();
        this.username = company.getSmtp().getUsername();
        this.password = company.getSmtp().getPassword();
    }
}
