package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.AdminCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<AdminCompany, UUID> {
    AdminCompany findByEmail(String email);

    boolean existsByEmail(String adminEmail);

    AdminCompany findByCompanyId(String companyId);
}
