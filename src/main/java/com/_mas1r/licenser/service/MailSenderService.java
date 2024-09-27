package com._mas1r.licenser.service;

import jakarta.mail.MessagingException;

public interface MailSenderService {
    void sendEmail(String to, String subject, String text) throws MessagingException;
}
