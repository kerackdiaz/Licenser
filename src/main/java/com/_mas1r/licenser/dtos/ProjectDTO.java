package com._mas1r.licenser.dtos;

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

    private String providerDomain;

    private String clientEmail;

    private String Description;

    private String projectName;

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

}
