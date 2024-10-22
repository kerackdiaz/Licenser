package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.MasterAdmin;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MasterAdminUpdateDTO {

    private String firstName;

    private String lastName;

    private String password;

    private String host;

    private int port;

    private String usernameSMTP;

    private String passwordSMTP;


    public MasterAdminUpdateDTO(MasterAdmin masterAdmin) {
        this.firstName = masterAdmin.getFirstName();
        this.lastName = masterAdmin.getLastName();
        this.host = masterAdmin.getSmtp().getHost();
        this.port = masterAdmin.getSmtp().getPort();
        this.usernameSMTP = masterAdmin.getSmtp().getUsername();
        this.passwordSMTP = masterAdmin.getSmtp().getPassword();
    }
}


