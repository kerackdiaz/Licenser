package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.*;
import com._mas1r.licenser.models.MasterAdmin;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UsersDTO> getAllUsers();

    String updateUser(UsersDTO usersDTO);

    MasterAdmin currentMaster();

    String updateMasterAdmin(MasterAdminUpdateDTO masterAdminDTO);

    String deleteUser(UUID id);
}
