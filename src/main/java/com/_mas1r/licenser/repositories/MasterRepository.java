package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.MasterAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MasterRepository extends JpaRepository<MasterAdmin, UUID> {
    MasterAdmin findByEmail(String email);

    boolean existsByEmail(String username);

    MasterAdmin findByUsername(String input);

    MasterAdmin findByPublicKey(String publicKeyEncoded);
}
