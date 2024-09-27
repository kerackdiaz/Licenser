package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.models.Project;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.service.MailSenderService;
import com._mas1r.licenser.service.SenderNotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderNotificationServiceImpl implements SenderNotificationService {

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private MasterRepository masterRepository;

    @Override
    public void sendCreationNotification(Project project) throws MessagingException {
        List<MasterAdmin> masterAdmins = masterRepository.findAll();

        for (MasterAdmin masterAdmin : masterAdmins) {
            mailSenderService.sendEmail(masterAdmin.getEmail(), "New project created", "A new project has been created: " + project.getProjectName());
        }
    }

    @Override
    public void sendExpirationNotification(Project project) throws MessagingException {
        List<MasterAdmin> masterAdmins = masterRepository.findAll();

        for (MasterAdmin masterAdmin : masterAdmins) {
            mailSenderService.sendEmail(masterAdmin.getEmail(), "License expiring soon", "A license project " + project.getProjectName() + " is expiring soon.");
        }
    }

    @Override
    public void sendRenewalNotification(Project project) throws MessagingException {
        List<MasterAdmin> masterAdmins = masterRepository.findAll();

        for (MasterAdmin masterAdmin : masterAdmins) {
            mailSenderService.sendEmail(masterAdmin.getEmail(), "License renewed", "The license project " + project.getProjectName() + " has been renewed.");
        }
    }
}