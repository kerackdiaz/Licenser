package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.dtos.SignInDTO;
import com._mas1r.licenser.dtos.SignUpDTO;
import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.models.UserCompany;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.CompanyRepository;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.security.JwtUtilService;
import com._mas1r.licenser.service.AuthenticacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthenticacionServiceImpl implements AuthenticacionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Override
    public Map<String, Object> Register(SignUpDTO signUpDTO, String email) {
        Map<String, Object> response = new HashMap<>();
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);

        try {
            if (signUpDTO.getFirstName().isBlank()) {
                response.put("message", "First name is empty");
                return response;
            }
            if (signUpDTO.getLastName().isBlank()) {
                response.put("message", "Last name is empty");
                return response;
            }
            if (signUpDTO.getUsername().isBlank()) {
                response.put("message", "Username is empty");
                return response;
            }
            if (signUpDTO.getUserEmail().isBlank()) {
                response.put("message", "Email is empty");
                return response;
            }
            if (signUpDTO.getPassword().isBlank()) {
                response.put("message", "Password is empty");
                return response;
            }
            if (userRepository.existsByEmail(signUpDTO.getUserEmail())) {
                response.put("message", "User already exists");
                return response;
            }
            if (adminCompany == null && masterAdmin == null) {
                response.put("message", "You do not have permission to perform this action");
                return response;
            }
            if (adminCompany != null) {
                Company company = adminCompany.getCompany();
                UserCompany user = new UserCompany();
                user.setFirstName(signUpDTO.getFirstName());
                user.setLastName(signUpDTO.getLastName());
                user.setUsername(signUpDTO.getUsername());
                user.setEmail(signUpDTO.getUserEmail());
                user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

                user.setCompany(company);
                user.setRole("USER");
                userRepository.save(user);
            }
            if (masterAdmin != null) {
                Company company = companyRepository.findById(UUID.fromString(signUpDTO.getId())).orElseThrow(() -> new Exception("Company not found"));
                UserCompany user = new UserCompany();
                user.setFirstName(signUpDTO.getFirstName());
                user.setLastName(signUpDTO.getLastName());
                user.setUsername(signUpDTO.getUsername());
                user.setEmail(signUpDTO.getUserEmail());
                user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
                user.setCompany(company);
                user.setRole("USER");
                userRepository.save(user);
            }

        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
        response.put("message", "User registered successfully");
        return response;
    }

    @Override
    public Map<String, Object> Login(SignInDTO signinDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (signinDTO.getUsername().isBlank()) {
                response.put("message", "Email is empty");
                return response;
            }
            if (signinDTO.getPassword().isBlank()) {
                response.put("message", "Password is empty");
                return response;
            }
            UserCompany user = userRepository.findByEmail(signinDTO.getUsername());
            AdminCompany admin = adminRepository.findByEmail(signinDTO.getUsername());
            MasterAdmin master = masterRepository.findByEmail(signinDTO.getUsername());

            if (user == null && admin == null && master == null) {
                response.put("message", "User does not exist");
                return response;
            }

            if (user != null) {
                if (!passwordEncoder.matches(signinDTO.getPassword(), user.getPassword())) {
                    response.put("message", "Password is incorrect");
                    return response;
                }
            } else if (admin != null) {
                if (!passwordEncoder.matches(signinDTO.getPassword(), admin.getPassword())) {
                    response.put("message", "Password is incorrect");
                    return response;
                }
            } else if (master != null) {
                if (!passwordEncoder.matches(signinDTO.getPassword(), master.getPassword())) {
                    response.put("message", "Password is incorrect");
                    return response;
                }
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinDTO.getUsername(), signinDTO.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(signinDTO.getUsername());
            final String jwt = jwtUtilService.generateToken(userDetails);
            response.put("message", jwt);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
        return response;
    }

    @Override
    public Map<String, Object> CreateCompany(RegisterCompanyDTO register, String email) {
        Map<String, Object> response = new HashMap<>();
        MasterAdmin masterAdmin = masterRepository.findByEmail(email);
        try{
            if(register.getCompanyName().isBlank()){
                response.put("message", "Company name is empty");
                return response;
            }
            if(register.getCompanyEmail().isBlank()){
                response.put("message", "Company email is empty");
                return response;
            }
            if(register.getCompanyLogo().isBlank()){
                response.put("message", "Company logo is empty");
                return response;
            }
            if(register.getAdminFirstName().isBlank()){
                response.put("message", "AdminCompany first name is empty");
                return response;
            }
            if(register.getAdminLastName().isBlank()){
                response.put("message", "AdminCompany last name is empty");
                return response;
            }
            if(register.getAdminUserName().isBlank()){
                response.put("message", "AdminCompany username is empty");
                return response;
            }
            if(register.getAdminEmail().isBlank()){
                response.put("message", "AdminCompany email is empty");
                return response;
            }
            if(register.getAdminPassword().isBlank()){
                response.put("message", "AdminCompany password is empty");
                return response;
            }
            if(adminRepository.existsByEmail(register.getAdminEmail())){
                response.put("message", "AdminCompany already exists");
                return response;
            }
            Company company = new Company();
            company.setCompanyName(register.getCompanyName());
            company.setCompanyEmail(register.getCompanyEmail());
            if (!register.getCompanyLogo().isBlank()){
                company.setCompanyLogo(register.getCompanyLogo());
            }
            companyRepository.save(company);
            AdminCompany adminCompany = new AdminCompany();
            adminCompany.setFirstName(register.getAdminFirstName());
            adminCompany.setLastName(register.getAdminLastName());
            adminCompany.setUsername(register.getAdminUserName());
            adminCompany.setEmail(register.getAdminEmail());
            adminCompany.setPassword(passwordEncoder.encode(register.getAdminPassword()));
            adminCompany.setCompany(company);
            adminRepository.save(adminCompany);
            response.put("message", "Company created successfully");

        }catch (Exception e){
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
        return response;
    }
}
