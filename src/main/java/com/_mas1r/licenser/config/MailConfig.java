package com._mas1r.licenser.config;
//
//import com._mas1r.licenser.models.*;
//import com._mas1r.licenser.repositories.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Properties;
//
//@Configuration
public class MailConfig {
//    @Autowired
//    private SMTPRepository smtpRepository;
//
//    @Autowired
//    private MasterRepository masterRepository;
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null) {
//            // Retornar una configuración predeterminada o lanzar una excepción
//            return getDefaultMailSender();
//        }
//
//        String email = authentication.getName();
//        SMTP smtp = getSMTPConfig(email);
//
//        if (smtp == null) {
//            // Retornar una configuración predeterminada o lanzar una excepción
//            return getDefaultMailSender();
//        }
//
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(smtp.getHost());
//        mailSender.setPort(smtp.getPort());
//        mailSender.setUsername(smtp.getUsername());
//        mailSender.setPassword(smtp.getPassword());  // No codificar la contraseña aquí
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//
//    private SMTP getSMTPConfig(String email) {
//        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
//        if (masterAdmin != null) {
//            return smtpRepository.findByMasterAdminId(masterAdmin.getId());
//        }
//
//        AdminCompany adminCompany = adminRepository.findByEmail(email);
//        if (adminCompany != null) {
//            return smtpRepository.findByCompanyId(adminCompany.getCompany().getId());
//        }
//
//        UserCompany userCompany = userRepository.findByEmail(email);
//        if (userCompany != null) {
//            return smtpRepository.findByCompanyId(userCompany.getCompany().getId());
//        }
//
//        return null;
//    }
//
//    private JavaMailSender getDefaultMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("default@example.com");
//        mailSender.setPassword("defaultPassword");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
}