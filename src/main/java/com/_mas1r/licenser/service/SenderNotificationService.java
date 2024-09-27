package com._mas1r.licenser.service;

import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.Project;
import jakarta.mail.MessagingException;

public interface SenderNotificationService {

    void sendCreationNotification(Project project) throws MessagingException;

    void sendExpirationNotification(Project project) throws MessagingException;

    void sendExpiredNotification(Project project) throws MessagingException;

    void sendRenewalNotification(Project project) throws MessagingException;

    void sendRegisterCompanyNotification(Company company) throws MessagingException;
}
