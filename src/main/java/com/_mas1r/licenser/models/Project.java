package com._mas1r.licenser.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    private String projectName;

    private String projectUrl;

    @Enumerated(EnumType.STRING)
    private ProyectType type;

    private String projectEmail;

    private String license;

    private Date initDate;

    private Date ExpDate;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    private String redirect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
