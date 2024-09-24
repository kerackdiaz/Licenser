package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findById(UUID id);
}
