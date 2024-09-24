package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.MasterAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MasterRepository extends JpaRepository<MasterAdmin, UUID> {
    MasterAdmin findByEmail(String email);

    boolean existsByEmail(String username);
}
