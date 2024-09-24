package com._mas1r.licenser.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

        private String username;

        private String email;

        private String password;

        private String role;
}
