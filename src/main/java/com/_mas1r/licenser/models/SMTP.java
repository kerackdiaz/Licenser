package com._mas1r.licenser.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMTP {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String host;

    private int port;

    private String username;

    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_admin_id")
    private MasterAdmin masterAdmin;
}
