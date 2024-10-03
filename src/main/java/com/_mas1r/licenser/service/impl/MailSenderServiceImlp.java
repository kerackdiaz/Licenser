package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.MailBodyDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
import com._mas1r.licenser.service.MailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MailSenderServiceImlp implements MailSenderService {

    @Autowired
    private SMTPRepository smtpRepository;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;


    @Override
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        JavaMailSender mailSender = getJavaMailSender();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        mailSender.send(message);
    }

    private JavaMailSender getJavaMailSender() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return getDefaultMailSender();
        }

        String email = authentication.getName();
        SMTP smtp = getSMTPConfig(email);

        if (smtp == null) {
            return getDefaultMailSender();
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtp.getHost());
        mailSender.setPort(smtp.getPort());
        mailSender.setUsername(smtp.getUsername());
        mailSender.setPassword(smtp.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private SMTP getSMTPConfig(String email) {
        MasterAdmin masterAdmin = masterRepository.findByEmail(email) != null ? masterRepository.findByEmail(email) : masterRepository.findByUsername(email);
        if (masterAdmin != null) {
            return smtpRepository.findByMasterAdminId(masterAdmin.getId());
        }

        AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
        if (adminCompany != null) {
            return smtpRepository.findByCompanyId(adminCompany.getCompany().getId());
        }

        UserCompany userCompany = userRepository.findByEmail(email) != null ? userRepository.findByEmail(email) : userRepository.findByUsername(email);
        if (userCompany != null) {
            return smtpRepository.findByCompanyId(userCompany.getCompany().getId());
        }

        return null;
    }

    private JavaMailSender getDefaultMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("default@example.com");
        mailSender.setPassword("defaultPassword");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    @Override
    public String createMailBody(MailBodyDTO mailBodyDTO){
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
    UserCompany userCompany = userRepository.findByEmail(email) != null ? userRepository.findByEmail(email) : userRepository.findByUsername(email);
        Company company = adminCompany.getCompany() != null ? adminCompany.getCompany() : userCompany.getCompany();
        EmailBody emailBody = new EmailBody();
        emailBody.setProjectType(ProjectType.valueOf(mailBodyDTO.getProjectType()));
        emailBody.setEmailType(EmailType.valueOf(mailBodyDTO.getEmailType()));
        emailBody.setSubject(mailBodyDTO.getSubject());
        emailBody.setBody(mailBodyDTO.getText());
        emailBody.setCompany(company);
        emailRepository.save(emailBody);
        return "Email created";
    }

    @Override
    public List<MailBodyDTO> getAllEmails(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
        UserCompany userCompany = userRepository.findByEmail(email) != null ? userRepository.findByEmail(email) : userRepository.findByUsername(email);
        Company company = adminCompany.getCompany() != null ? adminCompany.getCompany() : userCompany.getCompany();
        return company.getEmailBodies().stream().map(MailBodyDTO::new).collect(Collectors.toList());
    }

    @Override
    public String updateEmail(MailBodyDTO mailBodyDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
        UserCompany userCompany = userRepository.findByEmail(email) != null ? userRepository.findByEmail(email) : userRepository.findByUsername(email);
        Company company = adminCompany.getCompany() != null ? adminCompany.getCompany() : userCompany.getCompany();
        EmailBody emailBody = company.getEmailBodies().stream().filter(e -> e.getId().equals(UUID.fromString(mailBodyDTO.getId()))).findFirst().orElse(null);
        emailBody.setProjectType(ProjectType.valueOf(mailBodyDTO.getProjectType()));
        emailBody.setEmailType(EmailType.valueOf(mailBodyDTO.getEmailType()));
        emailBody.setSubject(mailBodyDTO.getSubject());
        emailBody.setBody(mailBodyDTO.getText());
        emailRepository.save(emailBody);
        return "Email updated";
    }

    @Override
    public String deleteEmail(UUID id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
        UserCompany userCompany = userRepository.findByEmail(email) != null ? userRepository.findByEmail(email) : userRepository.findByUsername(email);
        Company company = adminCompany.getCompany() != null ? adminCompany.getCompany() : userCompany.getCompany();
        EmailBody emailBody = company.getEmailBodies().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        emailRepository.delete(emailBody);
        return "Email deleted";
    }
}