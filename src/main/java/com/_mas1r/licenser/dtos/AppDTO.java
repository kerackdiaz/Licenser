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
public class AppDTO {

    private String clientName;

    private String clientAddress;

    private String clientPhone;

    private String ClientDni;

    private String providerHost;

    private String providerDomain;

    private String clientEmail;

    private String Description;

    private String projectName;

    private String projectUrl;

    private String license;

    private LocalDate initDate;

    private LocalDate ExpDate;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    private String redirect;

    private String paymentUrl;

    private double price;

    private double balance;

    private boolean statusLicense;

    private boolean statusProject;

    public AppDTO(Project project) {
        this.clientName = project.getClientName();
        this.clientAddress = project.getClientAddress();
        this.clientPhone = project.getClientPhone();
        this.ClientDni = project.getClientDni();
        this.providerHost = project.getProviderHost();
        this.providerDomain = project.getProviderDomain();
        this.clientEmail = project.getClientEmail();
        this.Description = project.getDescription();
        this.projectName = project.getProjectName();
        this.projectUrl = project.getProjectUrl();
        this.license = project.getLicense().getLicenseKey();
        this.initDate = project.getInitDate();
        this.ExpDate = project.getExpDate();
        this.licenseType = project.getLicenseType();
        this.redirect = project.getRedirect();
        this.paymentUrl = project.getPaymentUrl();
        this.price = project.getPrice();
        this.balance = project.getBalance();
        this.statusLicense = project.isStatusLicense();
        this.statusProject = project.isStatusProject();
    }


}