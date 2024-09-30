package com._mas1r.licenser.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String companyName;

    private String companyEmail;

    private String companyLogo;

    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AdminCompany adminCompany;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserCompany> userCompanies;

    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SMTP smtp;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmailBody> emailBodies;

    private boolean isActive;

    private String address;

    private String city;

    private String phone;

    private LocalDate creationDate;

    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private License license;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;

    private String whatsappToken;
}
