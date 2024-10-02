package com._mas1r.licenser.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(length = 1000)
    private String Description;

    private String projectName;

    private String projectUser;

    private String projectPassword;

    private String projectUrl;

    @Enumerated(EnumType.STRING)
    private ProjectType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "license_id")
    private License license;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
