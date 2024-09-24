package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserCompany, UUID> {
    UserCompany findByEmail(String email);

    boolean existsByEmail(String userEmail);
}
