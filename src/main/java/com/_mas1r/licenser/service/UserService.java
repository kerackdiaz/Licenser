package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.dtos.UsersDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<CompanyDTO> getAllCompanies();

    List<UsersDTO> getAllUsers(UUID companyId);
}
