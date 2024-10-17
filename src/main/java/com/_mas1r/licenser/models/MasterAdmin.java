package com._mas1r.licenser.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterAdmin {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        private String firstName;

        private String lastName;

        @Column(unique = true)
        private String username;

        @Column(unique = true)
        private String email;

        private String password;

        @OneToOne(mappedBy = "masterAdmin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private SMTP smtp;

        private String role;

        private String publicKey;

        private String privateKey;
}
