package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.models.SMTP;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MasterAdminDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private String host;

    private int port;

    private String usernameSMTP;

    private String passwordSMTP;

    private String publicKey;

    public MasterAdminDTO(MasterAdmin masterAdmin) {
        this.firstName = masterAdmin.getFirstName();
        this.lastName = masterAdmin.getLastName();
        this.username = masterAdmin.getUsername();
        this.email = masterAdmin.getEmail();
        this.host = masterAdmin.getSmtp().getHost();
        this.port = masterAdmin.getSmtp().getPort();
        this.usernameSMTP = masterAdmin.getSmtp().getUsername();
        this.passwordSMTP = masterAdmin.getSmtp().getPassword();
        this.publicKey = masterAdmin.getPublicKey();
    }
}
