package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private String clientName;

    private String clientAddress;

    private String clientPhone;

    private String ClientDni;

    private String providerHost;

    private String userHosting;

    private String passwordHosting;

    private String providerDomain;

    private String userDomain;

    private String passwordDomain;

    private String clientEmail;

    private String Description;

    private String projectName;

    private String projectUser;

    private String projectPassword;

    private String projectUrl;

    private String type;

    private LocalDate initDate;

    private LocalDate ExpDate;

    private String licenseType;

    private String redirect;

    private String paymentUrl;

    private double price;

    private double balance;

    private boolean statusLicense;

    private boolean statusProject;


    public ProjectDTO(Project project) {
    this.clientName = project.getClientName();
    this.clientAddress = project.getClientAddress();
    this.clientPhone = project.getClientPhone();
    this.ClientDni = project.getClientDni();
    this.providerHost = project.getProviderHost();
    this.userHosting = project.getUserHosting();
    this.passwordHosting = project.getPasswordHosting();
    this.providerDomain = project.getProviderDomain();
    this.userDomain = project.getUserDomain();
    this.passwordDomain = project.getPasswordDomain();
    this.clientEmail = project.getClientEmail();
    this.Description = project.getDescription();
    this.projectName = project.getProjectName();
    this.projectUser = project.getProjectUser();
    this.projectPassword = project.getProjectPassword();
    this.projectUrl = project.getProjectUrl();
    this.type = project.getType().name();
    this.initDate = project.getInitDate();
    this.ExpDate = project.getExpDate();
    this.licenseType = project.getLicenseType().name();
    this.redirect = project.getRedirect();
    this.paymentUrl = project.getPaymentUrl();
    this.price = project.getPrice();
    this.balance = project.getBalance();
    this.statusLicense = project.isStatusLicense();
    this.statusProject = project.isStatusProject();
    }

}
