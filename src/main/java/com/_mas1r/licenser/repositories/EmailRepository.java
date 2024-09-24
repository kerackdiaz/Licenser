package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.EmailBody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailBody, Long> {
}
