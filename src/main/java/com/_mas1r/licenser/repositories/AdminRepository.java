package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.AdminCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<AdminCompany, UUID> {
    AdminCompany findByEmail(String email);

    boolean existsByEmail(String adminEmail);
}
