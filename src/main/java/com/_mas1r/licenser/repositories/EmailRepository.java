package com._mas1r.licenser.repositories;

import com._mas1r.licenser.models.EmailBody;
import com._mas1r.licenser.models.EmailType;
import com._mas1r.licenser.models.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailBody, Long> {
    EmailBody findByProjectType(ProjectType projectType);

    List<EmailBody> findbyEmailType(EmailType emailType);
}
