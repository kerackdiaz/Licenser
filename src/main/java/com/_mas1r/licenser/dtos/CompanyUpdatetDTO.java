package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyUpdatetDTO {

    private String companyName;

    private String companyAddress;

    private String companyCity;

    private String companyPhone;

    private String companyEmail;

    private String urlLogo;

    private String primaryColor;

    private String secondaryColor;

    private String Status;

    private String whatsappToken;

    private String host;

    private int portHost;

    private String usernameHost;

    private String passwordHost;

    public CompanyUpdatetDTO(Company company) {
        this.companyName = company.getCompanyName();
        this.companyAddress = company.getAddress();
        this.companyCity = company.getCity();
        this.companyPhone = company.getPhone();
        this.companyEmail = company.getCompanyEmail();
        this.urlLogo = (company.getCompanyLogo() != null) ? company.getCompanyLogo() : "https://default.url/logo.png";
        this.Status = company.isActive() ? "Active" : "Inactive";
        this.whatsappToken = company.getWhatsappToken();
        this.host = company.getSmtp().getHost();
        this.portHost = company.getSmtp().getPort();
        this.usernameHost = company.getSmtp().getUsername();
        this.passwordHost = company.getSmtp().getPassword();
    }
}
