package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.ProjectDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.service.MailSenderService;
import com._mas1r.licenser.service.ProjectService;
import com._mas1r.licenser.service.SenderNotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.*;

@Service
public class ProjectServiceImpl implements ProjectService {


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private SenderNotificationService notificationService;


    @Scheduled(cron = "0 0 0 * * *")
    public void sendExpirationNotification(Project project) throws MessagingException {
        List<Project> projects = projectRepository.findAll();
        for (Project project1 : projects) {
            if (project1.getExpDate().isBefore(project1.getExpDate().minusDays(5))) {
                notificationService.sendExpirationNotification(project1);
                Company company = project1.getCompany();
                EmailBody emailBody = company.getEmailBodies().stream().anyMatch(e -> e.getProjectType().equals(project1.getType()) && e.getEmailType().equals(EmailType.LICENSE_ALERTE_EXPIRATION)) ? company.getEmailBodies().stream().filter(emailBody1 -> emailBody1.getProjectType().equals(project1.getType()) && emailBody1.getEmailType().equals(EmailType.LICENSE_ALERTE_EXPIRATION)).findFirst().orElse(null) : null;
                assert emailBody != null;
                mailSenderService.sendEmail(project.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void sendExpiredNotification(Project project) throws MessagingException {
        List<Project> projects = projectRepository.findAll();
        for (Project project1 : projects) {
            if (project1.getExpDate().equals(now())) {
                notificationService.sendExpiredNotification(project1);
                Company company = project1.getCompany();
                EmailBody emailBody = company.getEmailBodies().stream().anyMatch(e -> e.getProjectType().equals(project1.getType()) && e.getEmailType().equals(EmailType.LICENSE_EXPIRED)) ? company.getEmailBodies().stream().filter(emailBody1 -> emailBody1.getProjectType().equals(project1.getType()) && emailBody1.getEmailType().equals(EmailType.LICENSE_EXPIRED)).findFirst().orElse(null) : null;
                assert emailBody != null;
                mailSenderService.sendEmail(project.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
            }
        }
    }





    public String newProject(ProjectDTO project, String email) throws MessagingException {
        UUID companyId = null;
        if (adminRepository.existsByEmail(email)) {
            companyId = adminRepository.findByEmail(email).getCompany().getId();
        } else if (userRepository.existsByEmail(email)) {
            companyId = userRepository.findByEmail(email).getCompany().getId();
        }

        assert companyId != null;
        Company company = companyRepository.findById(companyId).orElse(null);

        Project newProject = new Project();
        newProject.setClientName(project.getClientName());
        newProject.setClientAddress(project.getClientAddress());
        newProject.setClientPhone(project.getClientPhone());
        newProject.setClientDni(project.getClientDni());
        newProject.setProviderHost(project.getProviderHost());
        newProject.setProviderDomain(project.getProviderDomain());
        newProject.setClientEmail(project.getClientEmail());
        newProject.setProjectName(project.getProjectName());
        newProject.setDescription(project.getDescription());
        newProject.setProjectUrl(project.getProjectUrl());
        newProject.setType(ProjectType.valueOf(project.getType()));
        newProject.setRedirect(project.getRedirect());
        newProject.setPaymentUrl(project.getPaymentUrl());
        newProject.setInitDate(project.getInitDate());
        newProject.setExpDate(licenseService.licenseExpiration(LicenseType.valueOf(project.getLicenseType()), project.getInitDate()));
        newProject.setLicenseType(LicenseType.valueOf(project.getLicenseType()));
        newProject.setPaymentUrl(project.getPaymentUrl());
        newProject.setPrice(project.getPrice());
        newProject.setBalance(project.getBalance());
        newProject.setStatusLicense(true);
        newProject.setStatusProject(true);
        newProject.setCompany(companyRepository.findById(companyId).orElse(null));
        projectRepository.save(newProject);

        newProject.setLicense(licenseService.createLicense(LicenseType.valueOf(project.getLicenseType()), project.getInitDate(), newProject));
        projectRepository.save(newProject);
        assert company != null;
        EmailBody emailBody = company.getEmailBodies().stream().anyMatch(e -> e.getProjectType().equals(newProject.getType()) && e.getEmailType().equals(EmailType.NEW_PROJECT)) ? company.getEmailBodies().stream().filter(emailBody1 -> emailBody1.getProjectType().equals(newProject.getType()) && emailBody1.getEmailType().equals(EmailType.NEW_PROJECT)).findFirst().orElse(null) : null;
        assert emailBody != null;
        mailSenderService.sendEmail(project.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
        notificationService.sendCreationNotification(newProject);
        return "Proyecto creado exitosamente";
    }



    public String renewProject(UUID projectId) throws MessagingException {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return "Project not found";
        }
        project.setInitDate(now());
        project.setExpDate(licenseService.licenseExpiration(project.getLicense().getLicenseType(), now()));
        projectRepository.save(project);
        project.setLicense(licenseService.renewLicense(projectId));
        projectRepository.save(project);
        Company company = project.getCompany();
        EmailBody emailBody = company.getEmailBodies().stream().anyMatch(e -> e.getProjectType().equals(project.getType()) && e.getEmailType().equals(EmailType.LICENSE_RENEWAL)) ? company.getEmailBodies().stream().filter(e -> e.getProjectType().equals(project.getType()) && e.getEmailType().equals(EmailType.LICENSE_RENEWAL)).findFirst().orElse(null) : null;
        assert emailBody != null;
        mailSenderService.sendEmail(project.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
        notificationService.sendRenewalNotification(project);
        return "Proyecto renovado exitosamente";
    }
}
