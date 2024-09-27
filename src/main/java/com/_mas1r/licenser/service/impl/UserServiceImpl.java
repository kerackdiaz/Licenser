package com._mas1r.licenser.service.impl;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.CompanyRepository;
import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(CompanyDTO::new).collect(Collectors.toList());
    }
}