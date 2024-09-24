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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserCompany user = userRepository.findByEmail(email);
        AdminCompany adminCompany = adminRepository.findByEmail(email);
        MasterAdmin superAdmin = masterRepository.findByEmail(email);

        if(user == null && adminCompany == null && superAdmin == null) {
            throw new UsernameNotFoundException(email);
        }

        if(adminCompany != null && user == null && superAdmin == null) {
            return User
                    .withUsername(email)
                    .password(adminCompany.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        if(superAdmin != null && user == null && adminCompany == null) {
            return User
                    .withUsername(email)
                    .password(superAdmin.getPassword())
                    .roles("ADMIN_SUPER")
                    .build();
        }

        assert user != null;
        return User
                .withUsername(email)
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}