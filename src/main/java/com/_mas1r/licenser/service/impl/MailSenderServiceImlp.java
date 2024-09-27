package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.models.SMTP;
import com._mas1r.licenser.models.UserCompany;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.repositories.SMTPRepository;
import com._mas1r.licenser.repositories.UserRepository;
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

import java.util.Properties;

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
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
        if (masterAdmin != null) {
            return smtpRepository.findByMasterAdminId(masterAdmin.getId());
        }

        AdminCompany adminCompany = adminRepository.findByEmail(email);
        if (adminCompany != null) {
            return smtpRepository.findByCompanyId(adminCompany.getCompany().getId());
        }

        UserCompany userCompany = userRepository.findByEmail(email);
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
}