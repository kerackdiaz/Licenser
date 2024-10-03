package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.EmailBody;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailBodyDTO {

    private String id;

    private String ProjectType;

    private String emailType;

    private String subject;

    private String text;

    public MailBodyDTO(EmailBody emailBody) {
        this.id = emailBody.getId().toString();
        this.ProjectType = emailBody.getProjectType().name();
        this.emailType = emailBody.getEmailType().name();
        this.subject = emailBody.getSubject();
        this.text = emailBody.getBody();
    }
}
