package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.SignInDTO;
import com._mas1r.licenser.dtos.SignUpDTO;
import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.*;
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
    AdminCompany adminCompany = adminRepository.findByEmail(email) != null ? adminRepository.findByEmail(email) : adminRepository.findByUsername(email);
    MasterAdmin masterAdmin = masterRepository.findByEmail(email)  != null ? masterRepository.findByEmail(email) : masterRepository.findByUsername(email);

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
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            response.put("message", "Username already exists");
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
            UserCompany user = userRepository.findByEmail(signinDTO.getUsername()) != null ? userRepository.findByEmail(signinDTO.getUsername()) : userRepository.findByUsername(signinDTO.getUsername());
            AdminCompany admin = adminRepository.findByEmail(signinDTO.getUsername()) != null ? adminRepository.findByEmail(signinDTO.getUsername()) : adminRepository.findByUsername(signinDTO.getUsername());
            MasterAdmin master = masterRepository.findByEmail(signinDTO.getUsername())  != null ? masterRepository.findByEmail(signinDTO.getUsername()) : masterRepository.findByUsername(signinDTO.getUsername());


            if (user == null && admin == null && master == null) {
                response.put("message", "User does not exist");
                return response;
            }

            if (user != null) {
                if (!passwordEncoder.matches(signinDTO.getPassword(), user.getPassword())) {
                    if (!user.getCompany().isActive()){
                        response.put("message", "Your License is expired, contact the platform administrator");
                        return response;
                    }
                    response.put("message", "Password is incorrect");
                    return response;
                }
            } else if (admin != null) {
                if (!passwordEncoder.matches(signinDTO.getPassword(), admin.getPassword())) {
                    if (!admin.getCompany().isActive()){
                        response.put("message", "Your License is expired, contact the platform administrator");
                        return response;
                    }
                    response.put("message", "Password is incorrect");
                    return response;
                }
            } else {
                if (!passwordEncoder.matches(signinDTO.getPassword(), master.getPassword())) {
                    response.put("message", "Password is incorrect");
                    return response;
                }
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinDTO.getUsername(), signinDTO.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(signinDTO.getUsername());
            final String jwt = jwtUtilService.generateToken(userDetails);
            response.put("token", jwt);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
        return response;
    }


    @Override
    public String recoveryPassword(UUID id, String password) {
        MasterAdmin masterAdmin = masterRepository.findById(id).orElse(null);
        AdminCompany adminCompany = adminRepository.findById(id).orElse(null);
        UserCompany userCompany = userRepository.findById(id).orElse(null);
        if (masterAdmin != null) {
            masterAdmin.setPassword(passwordEncoder.encode(password));
            return "Password updated successfully";
        } else if (adminCompany != null) {
            adminCompany.setPassword(passwordEncoder.encode(password));
            return "Password updated successfully";
        } else if (userCompany != null) {
            userCompany.setPassword(passwordEncoder.encode(password));
            return "Password updated successfully";
        }
        return "User not found";
    }
}
