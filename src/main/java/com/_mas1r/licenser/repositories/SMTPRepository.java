package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.SMTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SMTPRepository extends JpaRepository<SMTP, UUID> {
    SMTP findByCompanyId(UUID companyId);
    SMTP findByMasterAdminId(UUID masterAdminId);
}
