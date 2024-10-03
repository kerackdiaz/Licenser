package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.MasterAdminDTO;
import com._mas1r.licenser.dtos.UsersDTO;
import com._mas1r.licenser.models.MasterAdmin;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UsersDTO> getAllUsers();

    String updateUser(UsersDTO usersDTO);

    MasterAdmin currentMaster();

    String updateMasterAdmin(MasterAdminDTO masterAdminDTO);

    String deleteUser(UUID id);
}
