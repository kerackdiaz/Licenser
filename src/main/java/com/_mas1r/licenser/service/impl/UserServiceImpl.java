package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.MasterAdminDTO;
import com._mas1r.licenser.dtos.UsersDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
import com._mas1r.licenser.service.LicenseService;
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

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private SMTPRepository smtpRepository;



    @Override
    public List<UsersDTO> getAllUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        UserCompany user = userRepository.findByEmail(email);
        Company company = adminCompany != null ? adminCompany.getCompany() : user.getCompany();
        List<UsersDTO> users = company.getUserCompanies().stream().map(UsersDTO::new).collect(Collectors.toList());
        assert adminCompany != null;
        users.add(new UsersDTO(adminCompany));
        return users;
    }


    @Override
    public String updateUser(UsersDTO usersDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        UserCompany user = userRepository.findByEmail(email);

        if(adminCompany != null){
            if(!usersDTO.getFirstName().isBlank() && !adminCompany.getFirstName().equals(usersDTO.getFirstName())){
                adminCompany.setFirstName(usersDTO.getFirstName());
            }
            if(!usersDTO.getLastName().isBlank() && !adminCompany.getLastName().equals(usersDTO.getLastName())){
                adminCompany.setLastName(usersDTO.getLastName());
            }

            if(!usersDTO.getPassword().isBlank() && !adminCompany.getPassword().equals(usersDTO.getPassword())){
                adminCompany.setPassword(usersDTO.getPassword());
            }

            adminRepository.save(adminCompany);
        }
        if(user != null){
            if(!usersDTO.getFirstName().isBlank() && !user.getFirstName().equals(usersDTO.getFirstName())){
                user.setFirstName(usersDTO.getFirstName());
            }
            if(!usersDTO.getLastName().isBlank() && !user.getLastName().equals(usersDTO.getLastName())){
                user.setLastName(usersDTO.getLastName());
            }
            if(!usersDTO.getPassword().isBlank() && !user.getPassword().equals(usersDTO.getPassword())){
                user.setPassword(usersDTO.getPassword());
            }
            userRepository.save(user);
            return "User updated";
        }
        return "User not found";
    }

    @Override
    public MasterAdmin currentMaster() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return masterRepository.findByEmail(email);
    }



    @Override
    public String updateMasterAdmin(MasterAdminDTO masterAdminDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
        if(masterAdmin != null){
            if(!masterAdminDTO.getFirstName().isBlank() && !masterAdmin.getFirstName().equals(masterAdminDTO.getFirstName())){
                masterAdmin.setFirstName(masterAdminDTO.getFirstName());
            }
            if(!masterAdminDTO.getLastName().isBlank() && !masterAdmin.getLastName().equals(masterAdminDTO.getLastName())){
                masterAdmin.setLastName(masterAdminDTO.getLastName());
            }
            if(!masterAdminDTO.getUsername().isBlank() && !masterAdmin.getUsername().equals(masterAdminDTO.getUsername())){
                masterAdmin.setUsername(masterAdminDTO.getUsername());
            }
            if(!masterAdminDTO.getEmail().isBlank() && !masterAdmin.getEmail().equals(masterAdminDTO.getEmail())){
                masterAdmin.setEmail(masterAdminDTO.getEmail());
            }
            if(!masterAdminDTO.getPassword().isBlank() && !masterAdmin.getPassword().equals(masterAdminDTO.getPassword())){
                masterAdmin.setPassword(masterAdminDTO.getPassword());
            }

            //masterAdmin SMTP Data
            if(masterAdmin.getSmtp() == null){
                SMTP smtp = new SMTP();
                smtp.setMasterAdmin(masterAdmin);
                smtpRepository.save(smtp);
                masterAdmin.setSmtp(smtp);
                masterRepository.save(masterAdmin);
            }

            if(!masterAdminDTO.getHost().isBlank() && !masterAdmin.getSmtp().getHost().equals(masterAdminDTO.getHost())){
                masterAdmin.getSmtp().setHost(masterAdminDTO.getHost());
            }
            if(masterAdminDTO.getPort() != 0 && masterAdmin.getSmtp().getPort() != masterAdminDTO.getPort()){
                masterAdmin.getSmtp().setPort(masterAdminDTO.getPort());
            }
            if(!masterAdminDTO.getUsernameSMTP().isBlank() && !masterAdmin.getSmtp().getUsername().equals(masterAdminDTO.getUsernameSMTP())){
                masterAdmin.getSmtp().setUsername(masterAdminDTO.getUsernameSMTP());
            }
            if(!masterAdminDTO.getPasswordSMTP().isBlank() && !masterAdmin.getSmtp().getPassword().equals(masterAdminDTO.getPasswordSMTP())){
                masterAdmin.getSmtp().setPassword(masterAdminDTO.getPasswordSMTP());
            }
            masterRepository.save(masterAdmin);
            return "Master Admin updated";
        }
        return "Master Admin not found";
    }


    @Override
    public String deleteUser(UUID id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        if(adminCompany != null){
            UserCompany userCompany = userRepository.findById(id).orElse(null);
            if(userCompany != null){
                userRepository.delete(userCompany);
                return "User deleted";
            }
        }
        return "User not found";
    }
}
