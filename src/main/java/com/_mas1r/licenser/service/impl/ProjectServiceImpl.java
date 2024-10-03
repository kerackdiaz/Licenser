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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.time.LocalDate.*;

@Service
public class ProjectServiceImpl implements ProjectService {


    @Autowired
    private ProjectRepository projectRepository;


    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseService licenseService;


    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private SenderNotificationService notificationService;



    @Scheduled(cron = "0 0 0 * * *")
    public void sendExpirationNotification() throws MessagingException {
        List<Project> projects = projectRepository.findAll();
        for (Project project1 : projects) {
            if (project1.getExpDate().isBefore(project1.getExpDate().minusDays(5))) {
                notificationService.sendExpirationNotification(project1);
                Company company = project1.getCompany();
                EmailBody emailBody = company.getEmailBodies().stream().anyMatch(e -> e.getProjectType().equals(project1.getType()) && e.getEmailType().equals(EmailType.LICENSE_ALERTE_EXPIRATION)) ? company.getEmailBodies().stream().filter(emailBody1 -> emailBody1.getProjectType().equals(project1.getType()) && emailBody1.getEmailType().equals(EmailType.LICENSE_ALERTE_EXPIRATION)).findFirst().orElse(null) : null;
                assert emailBody != null;
                mailSenderService.sendEmail(project1.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void sendExpiredNotification() throws MessagingException {
        List<Project> projects = projectRepository.findAll();
        for (Project project1 : projects) {
            if (project1.getExpDate().isBefore(now())) {
                project1.setStatusLicense(false);
                notificationService.sendExpiredNotification(project1);
                Company company = project1.getCompany();
                EmailBody emailBody = company.getEmailBodies().stream().anyMatch(e -> e.getProjectType().equals(project1.getType()) && e.getEmailType().equals(EmailType.LICENSE_EXPIRED)) ? company.getEmailBodies().stream().filter(emailBody1 -> emailBody1.getProjectType().equals(project1.getType()) && emailBody1.getEmailType().equals(EmailType.LICENSE_EXPIRED)).findFirst().orElse(null) : null;
                assert emailBody != null;
                mailSenderService.sendEmail(project1.getClientEmail(),emailBody.getSubject(),emailBody.getBody());
            }
        }
    }




    @Override
    public String newProject(ProjectDTO project) throws MessagingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCompany user = userRepository.findByEmail(email);
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        Company company = adminCompany != null ? adminCompany.getCompany() : user != null ? user.getCompany() : null;

        if (company == null) {
            return "Company not found";
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
        newProject.setInitDate(project.getInitDate());
        newProject.setExpDate(licenseService.licenseExpiration(LicenseType.valueOf(project.getLicenseType()), project.getInitDate()));
        newProject.setLicenseType(LicenseType.valueOf(project.getLicenseType()));
        newProject.setPaymentUrl(project.getPaymentUrl());
        newProject.setPrice(project.getPrice());
        newProject.setBalance(project.getBalance());
        newProject.setStatusLicense(true);
        newProject.setStatusProject(true);
        newProject.setCompany(company);
        projectRepository.save(newProject);

        License license = licenseService.createLicense(LicenseType.valueOf(project.getLicenseType()), project.getInitDate(), newProject, null);
        newProject.setLicense(license);
        projectRepository.save(newProject);

        EmailBody emailBody = company.getEmailBodies().stream()
                .filter(e -> e.getProjectType().equals(newProject.getType()) && e.getEmailType().equals(EmailType.NEW_PROJECT))
                .findFirst()
                .orElse(null);

        if (emailBody != null) {
            mailSenderService.sendEmail(project.getClientEmail(), emailBody.getSubject(), emailBody.getBody());
        }

        notificationService.sendCreationNotification(newProject);
        return "Proyecto creado exitosamente";
    }


    @Override
    public String renewProject(Long projectId) throws MessagingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCompany user = userRepository.findByEmail(email);
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        Company company = adminCompany != null ? adminCompany.getCompany() : user != null ? user.getCompany() : null;

        if (company == null) {
            return "Company not found";
        }

        Project project = company.getProjects().stream()
                .filter(p -> p.getId().equals(projectId))
                .findFirst()
                .orElse(null);

        if (project == null) {
            return "Project not found";
        }

        project.setExpDate(licenseService.licenseExpiration(project.getLicense().getLicenseType(), now()));
        projectRepository.save(project);
        project.setLicense(licenseService.renewLicense(projectId));
        projectRepository.save(project);

        EmailBody emailBody = company.getEmailBodies().stream()
                .filter(e -> e.getProjectType().equals(project.getType()) && e.getEmailType().equals(EmailType.LICENSE_RENEWAL))
                .findFirst()
                .orElse(null);

        if (emailBody != null) {
            mailSenderService.sendEmail(project.getClientEmail(), emailBody.getSubject(), emailBody.getBody());
        }

        notificationService.sendRenewalNotification(project);
        return "Proyecto renovado exitosamente";
    }



@Override
public String updateProject(Long projectId, ProjectDTO projectDTO) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    UserCompany user = userRepository.findByEmail(email);
    AdminCompany adminCompany = adminRepository.findByEmail(email);
    Company company = adminCompany != null ? adminCompany.getCompany() : user != null ? user.getCompany() : null;

    assert company != null;
    Project project = company.getProjects().stream()
            .filter(p -> p.getId().equals(projectId))
            .findFirst()
            .orElse(null);

    if (project == null) {
        return "Project not found";
    }

    if (!projectDTO.getClientName().isBlank() && !projectDTO.getClientName().equals(project.getClientName())) {
        project.setClientName(projectDTO.getClientName());
    }
    if (!projectDTO.getClientAddress().isBlank() && !projectDTO.getClientAddress().equals(project.getClientAddress())) {
        project.setClientAddress(projectDTO.getClientAddress());
    }
    if (!projectDTO.getClientPhone().isBlank() && !projectDTO.getClientPhone().equals(project.getClientPhone())) {
        project.setClientPhone(projectDTO.getClientPhone());
    }
    if (!projectDTO.getClientDni().isBlank() && !projectDTO.getClientDni().equals(project.getClientDni())) {
        project.setClientDni(projectDTO.getClientDni());
    }
    if (!projectDTO.getProviderHost().isBlank() && !projectDTO.getProviderHost().equals(project.getProviderHost())) {
        project.setProviderHost(projectDTO.getProviderHost());
    }
    if (!projectDTO.getUserHosting().isBlank() && !projectDTO.getUserHosting().equals(project.getUserHosting())) {
        project.setUserHosting(projectDTO.getUserHosting());
    }
    if (!projectDTO.getPasswordHosting().isBlank() && !projectDTO.getPasswordHosting().equals(project.getPasswordHosting())) {
        project.setPasswordHosting(projectDTO.getPasswordHosting());
    }
    if (!projectDTO.getProviderDomain().isBlank() && !projectDTO.getProviderDomain().equals(project.getProviderDomain())) {
        project.setProviderDomain(projectDTO.getProviderDomain());
    }
    if (!projectDTO.getUserDomain().isBlank() && !projectDTO.getUserDomain().equals(project.getUserDomain())) {
        project.setUserDomain(projectDTO.getUserDomain());
    }
    if (!projectDTO.getPasswordDomain().isBlank() && !projectDTO.getPasswordDomain().equals(project.getPasswordDomain())) {
        project.setPasswordDomain(projectDTO.getPasswordDomain());
    }
    if (!projectDTO.getClientEmail().isBlank() && !projectDTO.getClientEmail().equals(project.getClientEmail())) {
        project.setClientEmail(projectDTO.getClientEmail());
    }
    if (!projectDTO.getProjectName().isBlank() && !projectDTO.getProjectName().equals(project.getProjectName())) {
        project.setProjectName(projectDTO.getProjectName());
    }
    if (!projectDTO.getProjectUser().isBlank() && !projectDTO.getProjectUser().equals(project.getProjectUser())) {
        project.setProjectUser(projectDTO.getProjectUser());
    }
    if (!projectDTO.getProjectPassword().isBlank() && !projectDTO.getProjectPassword().equals(project.getProjectPassword())) {
        project.setProjectPassword(projectDTO.getProjectPassword());
    }
    if (!projectDTO.getProjectUrl().isBlank() && !projectDTO.getProjectUrl().equals(project.getProjectUrl())) {
        project.setProjectUrl(projectDTO.getProjectUrl());
    }
    if (!projectDTO.getRedirect().isBlank() && !projectDTO.getRedirect().equals(project.getRedirect())) {
        project.setRedirect(projectDTO.getRedirect());
    }
    if (!projectDTO.getPaymentUrl().isBlank() && !projectDTO.getPaymentUrl().equals(project.getPaymentUrl())) {
        project.setPaymentUrl(projectDTO.getPaymentUrl());
    }
    if (projectDTO.getBalance() != 0 && projectDTO.getBalance() <= project.getPrice()) {
        project.setBalance(projectDTO.getBalance());
    }
    if (projectDTO.isStatusProject() != project.isStatusProject()) {
        project.setStatusProject(projectDTO.isStatusProject());
    }
    projectRepository.save(project);

    if (!project.isStatusProject() && project.getBalance() == project.getPrice()) {
        return "Project successfully finalized";
    }
    return "Project updated successfully";
}

@Override
public ProjectDTO getProject(Long id) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    UserCompany user = userRepository.findByEmail(email) != null ? userRepository.findByEmail(email) : userRepository.findByUsername(email);
    AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
    Company company = adminCompany != null ? adminCompany.getCompany() : user != null ? user.getCompany() : null;

    if (company == null) {
        throw new IllegalArgumentException("Company not found");
    }

    Project project = company.getProjects().stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElse(null);

    if (project == null) {
        throw new IllegalArgumentException("Project not found");
    }

    return new ProjectDTO(project);
}

}
