// src/main/java/com/_mas1r/licenser/security/UserDetailsServiceImplements.java
package com._mas1r.licenser.security;

import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.models.UserCompany;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplements implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MasterRepository masterRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

        UserCompany user = userRepository.findByEmail(input);
        AdminCompany adminCompany = adminRepository.findByEmail(input);
        MasterAdmin superAdmin = masterRepository.findByEmail(input);

        if(superAdmin == null && user == null && adminCompany == null) {
           user = userRepository.findByUsername(input);
              adminCompany = adminRepository.findByUsername(input);
                superAdmin = masterRepository.findByUsername(input);
        }


        if(user == null && adminCompany == null && superAdmin == null) {
            throw new UsernameNotFoundException(input);
        }

        if(adminCompany != null ) {
            return User
                    .withUsername(input)
                    .password(adminCompany.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        if(superAdmin != null) {
            return User
                    .withUsername(input)
                    .password(superAdmin.getPassword())
                    .roles("ADMIN_SUPER")
                    .build();
        }

        return User
                .withUsername(input)
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}