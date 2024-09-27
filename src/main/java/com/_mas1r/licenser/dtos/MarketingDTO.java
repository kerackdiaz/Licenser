package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.LicenseType;
import com._mas1r.licenser.models.Project;
import com._mas1r.licenser.models.ProjectType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MarketingDTO {

    private String clientName;

    private String clientPhone;

    private String providerHost;

    private String providerDomain;

    private String clientEmail;

    private String Description;

    private String projectName;

    private String projectUrl;

    private LocalDate initDate;

    private LocalDate ExpDate;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    private String paymentUrl;

    private double price;

    private double balance;

    private boolean statusLicense;

    private boolean statusProject;

    public MarketingDTO(Project project) {
        this.clientName = project.getClientName();
        this.clientPhone = project.getClientPhone();
        this.providerHost = project.getProviderHost();
        this.providerDomain = project.getProviderDomain();
        this.clientEmail = project.getClientEmail();
        this.Description = project.getDescription();
        this.projectName = project.getProjectName();
        this.projectUrl = project.getProjectUrl();
        this.initDate = project.getInitDate();
        this.ExpDate = project.getExpDate();
        this.licenseType = project.getLicenseType();
        this.paymentUrl = project.getPaymentUrl();
        this.price = project.getPrice();
        this.balance = project.getBalance();
        this.statusLicense = project.isStatusLicense();
        this.statusProject = project.isStatusProject();
    }


}
