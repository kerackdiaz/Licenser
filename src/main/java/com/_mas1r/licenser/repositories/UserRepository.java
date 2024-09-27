package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserCompany, UUID> {
    UserCompany findByEmail(String email);

    boolean existsByEmail(String userEmail);

    List<UserCompany> findByCompanyId(String id);
}
