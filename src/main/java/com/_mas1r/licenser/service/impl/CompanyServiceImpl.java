package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
import com._mas1r.licenser.service.CompanyService;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.service.SenderNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SMTPRepository smtpRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private SenderNotificationService senderNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public List<CompanyExtractDTO> getAllCompanies() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
        if (masterAdmin != null) {
            return companyRepository.findAll().stream().map(CompanyExtractDTO::new).collect(Collectors.toList());
        }
        return null;
    }


    @Override
    public CompanyDTO currentCompany(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        UserCompany user = userRepository.findByEmail(email);
        Company company = adminCompany != null ? adminCompany.getCompany() : user.getCompany();

        return new CompanyDTO(company);
    }


    @Override
    public Map<String, Object> CreateCompany(RegisterCompanyDTO register) {
        Map<String, Object> response = new HashMap<>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);

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

            AdminCompany adminCompany = new AdminCompany();
            adminCompany.setFirstName(register.getAdminFirstName());
            adminCompany.setLastName(register.getAdminLastName());
            adminCompany.setUsername(register.getAdminUserName());
            adminCompany.setEmail(register.getAdminEmail());
            adminCompany.setPassword(passwordEncoder.encode(register.getAdminPassword()));
            adminCompany.setCompany(company);
            adminRepository.save(adminCompany);

            senderNotificationService.sendRegisterCompanyNotification(company);
            response.put("message", "Company created successfully");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }




    @Override
    public String updateCompany(String id, CompanyDTO companyDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
        AdminCompany adminCompany = adminRepository.findByEmail(email);

        Company company = companyRepository.findById(UUID.fromString(id)).orElse(null);
        if (company == null) {
            return "Company not found";
        }
        //company basic data
        if(!companyDTO.getName().isBlank() && !company.getCompanyName().equals(companyDTO.getName())){
            company.setCompanyName(companyDTO.getName());
        }
        if(!companyDTO.getAddress().isBlank() && !company.getAddress().equals(companyDTO.getAddress())){
            company.setAddress(companyDTO.getAddress());
        }
        if(!companyDTO.getCity().isBlank() && !company.getCity().equals(companyDTO.getCity())){
            company.setCity(companyDTO.getCity());
        }
        if(!companyDTO.getPhone().isBlank() && !company.getPhone().equals(companyDTO.getPhone())){
            company.setPhone(companyDTO.getPhone());
        }
        if(!companyDTO.getEmail().isBlank() && !company.getCompanyEmail().equals(companyDTO.getEmail())){
            company.setCompanyEmail(companyDTO.getEmail());
        }
        //company personalitation data
        if(!companyDTO.getUrlLogo().isBlank() && !company.getCompanyLogo().equals(companyDTO.getUrlLogo())){
            company.setCompanyLogo(companyDTO.getUrlLogo());
        }
        if(!companyDTO.getPrimaryColor().isBlank() && !company.getPrimaryColor().equals(companyDTO.getPrimaryColor())){
            company.setPrimaryColor(companyDTO.getPrimaryColor());
        }
        if(!companyDTO.getSecondaryColor().isBlank() && !company.getSecondaryColor().equals(companyDTO.getSecondaryColor())){
            company.setSecondaryColor(companyDTO.getSecondaryColor());
        }
        //company Settings Data
        if(!companyDTO.getCompanyKeyId().isBlank() && !company.getCompanyKeyId().equals(companyDTO.getCompanyKeyId())){
            company.setCompanyKeyId(companyDTO.getCompanyKeyId());
        }
        if(!companyDTO.getWhatsappToken().isBlank() && !company.getWhatsappToken().equals(companyDTO.getWhatsappToken())){
            company.setWhatsappToken(companyDTO.getWhatsappToken());
        }
        companyRepository.save(company);
        //company SMTP Data
        if(company.getSmtp() == null){
            SMTP smtp = new SMTP();
            smtp.setCompany(company);
            smtpRepository.save(smtp);
            company.setSmtp(smtp);
            companyRepository.save(company);
        }
        if (!company.getSmtp().getHost().equals(companyDTO.getHost()) || company.getSmtp().getPort() != companyDTO.getPort() || !company.getSmtp().getUsername().equals(companyDTO.getUsername()) || !company.getSmtp().getPassword().equals(companyDTO.getPassword())) {
            SMTP smtp = company.getSmtp();
            if(!companyDTO.getHost().isBlank() && !company.getSmtp().getHost().equals(companyDTO.getHost())){
                smtp.setHost(companyDTO.getHost());
            }
            if(companyDTO.getPort() != 0 && company.getSmtp().getPort() != companyDTO.getPort()){
                smtp.setPort(companyDTO.getPort());
            }
            if(!companyDTO.getUsername().isBlank() && !company.getSmtp().getUsername().equals(companyDTO.getUsername())){
                smtp.setUsername(companyDTO.getUsername());
            }
            if(!companyDTO.getPassword().isBlank() && !company.getSmtp().getPassword().equals(companyDTO.getPassword())){
                smtp.setPassword(companyDTO.getPassword());
            }
            smtpRepository.save(smtp);
            return "SMTP updated";
        }
        return "Company updated";
    }
}
