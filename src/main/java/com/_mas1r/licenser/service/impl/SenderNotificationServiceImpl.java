package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.Project;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.ProjectRepository;
import com._mas1r.licenser.service.MailSenderService;
import com._mas1r.licenser.service.SenderNotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderNotificationServiceImpl implements SenderNotificationService {

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void sendCreationNotification(Project project) throws MessagingException {
        AdminCompany admin = project.getCompany().getAdminCompany();

        mailSenderService.sendEmail(admin.getEmail(), "New project created", "A new project has been created: " + project.getProjectName());

    }

    @Override
    public void sendExpirationNotification(Project project) throws MessagingException {
        AdminCompany admin = project.getCompany().getAdminCompany();

        mailSenderService.sendEmail(admin.getEmail(), "License expiring soon", "A license project " + project.getProjectName() + " is expiring soon.");
    }

    @Override
    public void sendExpiredNotification(Project project) throws MessagingException {
        AdminCompany admin = project.getCompany().getAdminCompany();

        mailSenderService.sendEmail(admin.getEmail(), "License a expired", "A license project " + project.getProjectName() + " is expiring soon.");
    }

    @Override
    public void sendRenewalNotification(Project project) throws MessagingException {
        AdminCompany admin = project.getCompany().getAdminCompany();

        mailSenderService.sendEmail(admin.getEmail(), "License renewed", "The license project " + project.getProjectName() + " has been renewed.");
    }

    @Override
    public void sendRegisterCompanyNotification(Company company) throws MessagingException {
        mailSenderService.sendEmail(company.getCompanyEmail(), "Hola " + company.getCompanyName(), "The company has been registered successfully.");
    }


}