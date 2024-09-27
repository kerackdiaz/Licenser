package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.ProjectDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.service.MailSenderService;
import com._mas1r.licenser.service.ProjectService;
import com._mas1r.licenser.service.SenderNotificationService;
import com._mas1r.licenser.utils.SerialKey;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private SerialKey Key;

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

    public String newProject(ProjectDTO project, String email) throws MessagingException {
        UUID companyId = null;
        if (adminRepository.existsByEmail(email)) {
            companyId = adminRepository.findByEmail(email).getCompany().getId();
        } else if (userRepository.existsByEmail(email)) {
            companyId = userRepository.findByEmail(email).getCompany().getId();
        }

        Company company = companyRepository.findById(companyId).orElse(null);

        if (companyId == null) {
            return "Company not found for the given email";
        }

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
        EmailBody emailBody = emailRepository.findByProjectType(company.getEmailBodies().stream().map(EmailBody::getProjectType).filter(projectType -> projectType.equals(newProject.getType())).findFirst().orElse(null));
        mailSenderService.sendEmail(project.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
        notificationService.sendCreationNotification(newProject);
        return "Proyecto creado exitosamente";
    }

    public String renewProject(UUID projectId) throws MessagingException {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return "Project not found";
        }
        project.setInitDate(LocalDate.now());
        project.setExpDate(licenseService.licenseExpiration(project.getLicense().getLicenseType(), LocalDate.now()));
        projectRepository.save(project);
        project.setLicense(licenseService.renewLicense(projectId));
        projectRepository.save(project);
        Company company = project.getCompany();
        EmailBody emailBody = emailRepository.findByProjectType(company.getEmailBodies().stream().map(EmailBody::getProjectType).filter(projectType -> projectType.equals(project.getType())).findFirst().orElse(null));
        mailSenderService.sendEmail(project.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
        notificationService.sendRenewalNotification(project);
        return "Proyecto renovado exitosamente";
    }
}
