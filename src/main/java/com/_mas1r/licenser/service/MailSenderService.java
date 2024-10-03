package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.MailBodyDTO;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.UUID;

public interface MailSenderService {
    void sendEmail(String to, String subject, String text) throws MessagingException;

    String createMailBody(MailBodyDTO mailBodyDTO);

    List<MailBodyDTO> getAllEmails();

    String updateEmail(MailBodyDTO mailBodyDTO);

    String deleteEmail(UUID id);
}
