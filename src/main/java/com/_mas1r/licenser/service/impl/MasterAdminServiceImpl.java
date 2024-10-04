
package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.service.MasterAdminService;
import com._mas1r.licenser.service.SenderNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MasterAdminServiceImpl implements MasterAdminService {

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private SMTPRepository smtpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private SenderNotificationService senderNotificationService;

@Override
public void generateAndStoreKeys() throws NoSuchAlgorithmException {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    MasterAdmin masterAdmin = masterRepository.findByEmail(email) != null ? masterRepository.findByEmail(email) : masterRepository.findByUsername(email);

    if (masterAdmin.getPublicKey() == null || masterAdmin.getPublicKey().isEmpty()) {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstanceStrong(); // Use a strong source of randomness
        keyGen.initialize(256, random); // 256 bits for ECC
        KeyPair pair = keyGen.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());

        masterAdmin.setPublicKey(publicKey); // Store the full public key
        masterAdmin.setPrivateKey(privateKey);

        masterRepository.save(masterAdmin);
    }
}

    @Override
    public List<CompanyExtractDTO> getAllCompanies(String publicKey) {
        MasterAdmin masterAdmin = masterRepository.findByPublicKey(publicKey);
        if (masterAdmin != null) {
            return companyRepository.findAll().stream().map(CompanyExtractDTO::new).collect(Collectors.toList());
        }
        return null;
    }



    @Override
    public Map<String, Object> createNewCompany(String publicKey, RegisterCompanyDTO register) {
        Map<String, Object> response = new HashMap<>();
        MasterAdmin masterAdmin = masterRepository.findByPublicKey(publicKey);

        if (masterAdmin == null) {
            response.put("message", "You do not have permission to perform this action");
            return response;
        }

        if (register.getCompanyName().isBlank()) {
            response.put("message", "Company name is empty");
            return response;
        }
        if (register.getCompanyEmail().isBlank()) {
            response.put("message", "Company email is empty");
            return response;
        }
        if (register.getAdminFirstName().isBlank()) {
            response.put("message", "AdminCompany first name is empty");
            return response;
        }
        if (register.getAdminLastName().isBlank()) {
            response.put("message", "AdminCompany last name is empty");
            return response;
        }
        if (register.getAdminUserName().isBlank()) {
            response.put("message", "AdminCompany username is empty");
            return response;
        }
        if (register.getAdminEmail().isBlank()) {
            response.put("message", "AdminCompany email is empty");
            return response;
        }
        if (register.getAdminPassword().isBlank()) {
            response.put("message", "AdminCompany password is empty");
            return response;
        }
        if (adminRepository.existsByEmail(register.getAdminEmail())) {
            response.put("message", "AdminCompany already exists");
            return response;
        }

        try {
            Company company = new Company();
            company.setCompanyName(register.getCompanyName());
            company.setCompanyEmail(register.getCompanyEmail());
            company.setCreationDate(LocalDate.now());
            company.setActive(true);
            companyRepository.save(company);

            License license = licenseService.createLicense(LicenseType.valueOf(register.getLicenseType()), LocalDate.now(), null, company);
            company.setLicense(license);
            licenseRepository.save(license);
            companyRepository.save(company);

            SMTP smtp = new SMTP();
            smtp.setHost("Set Data");
            smtp.setPort(465);
            smtp.setUsername("Set Data");
            smtp.setPassword("Set Data");
            smtp.setCompany(company);
            smtpRepository.save(smtp);
            company.setSmtp(smtp);
            companyRepository.save(company);

            AdminCompany adminCompany = new AdminCompany();
            adminCompany.setFirstName(register.getAdminFirstName());
            adminCompany.setLastName(register.getAdminLastName());
            adminCompany.setUsername(register.getAdminUserName());
            adminCompany.setEmail(register.getAdminEmail());
            adminCompany.setPassword(passwordEncoder.encode(register.getAdminPassword()));
            adminCompany.setCompany(company);
            adminCompany.setRole("ADMIN");
            adminRepository.save(adminCompany);

            senderNotificationService.sendRegisterCompanyNotification(company);
            response.put("message", "Company created successfully");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }


    @Override
    public Map<String, Object> changeStatusCompany(String publicKey, UUID companyId) {
        Map<String, Object> response = new HashMap<>();
        MasterAdmin masterAdmin = masterRepository.findByPublicKey(publicKey);

        if (masterAdmin == null) {
            response.put("message", "You do not have permission to perform this action");
            return response;
        }

        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            response.put("message", "Company not found");
            return response;
        }

        company.setActive(!company.isActive());
        companyRepository.save(company);
        response.put("message", "Company status updated successfully");
        return response;
    }


    @Override
    public Map<String, Object> renewalLicense(String publicKey, UUID companyId, LicenseCompanyStatus licenseStatus) {
        Map<String, Object> response = new HashMap<>();
        MasterAdmin masterAdmin = masterRepository.findByPublicKey(publicKey);

        if (masterAdmin == null) {
            response.put("message", "You do not have permission to perform this action");
            return response;
        }

        String result = licenseService.updateLicenseCompany(companyId.toString(), licenseStatus);
        response.put("message", result);
        return response;
    }

}