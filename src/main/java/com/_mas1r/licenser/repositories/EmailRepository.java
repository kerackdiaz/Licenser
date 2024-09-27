package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.EmailBody;
import com._mas1r.licenser.models.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailBody, Long> {
    EmailBody findByProjectType(ProjectType projectType);
}
