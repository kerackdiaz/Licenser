package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.dtos.UsersDTO;
import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.CompanyRepository;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MasterRepository masterRepository;


    @Override
    public List<CompanyDTO> getAllCompanies() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
        if (masterAdmin != null) {
            return companyRepository.findAll().stream().map(CompanyDTO::new).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<UsersDTO> getAllUsers(UUID companyId) {
        List<UsersDTO> users = userRepository.findByCompanyId(companyId).stream()
                .map(UsersDTO::new)
                .collect(Collectors.toList());

        AdminCompany adminCompany = adminRepository.findByCompanyId(companyId);
        if (adminCompany != null) {
            users.add(new UsersDTO(adminCompany));
        }

        return users;
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
        if(!companyDTO.getName().isBlank() || !company.getCompanyName().equals(companyDTO.getName())){
            company.setCompanyName(companyDTO.getName());
        }
        if(!companyDTO.getAddress().isBlank() || !company.getAddress().equals(companyDTO.getAddress())){
            company.setAddress(companyDTO.getAddress());
        }
        if(!companyDTO.getCity().isBlank() || !company.getCity().equals(companyDTO.getCity())){
            company.setCity(companyDTO.getCity());
        }
        if(!companyDTO.getPhone().isBlank() || !company.getPhone().equals(companyDTO.getPhone())){
            company.setPhone(companyDTO.getPhone());
        }
        if(!companyDTO.getEmail().isBlank() || !company.getCompanyEmail().equals(companyDTO.getEmail())){
            company.setCompanyEmail(companyDTO.getEmail());
        }
        if(!companyDTO.getUrlLogo().isBlank() || !company.getCompanyLogo().equals(companyDTO.getUrlLogo())){
            company.setCompanyLogo(companyDTO.getUrlLogo());
        }

            return "Company updated";

    }
}
