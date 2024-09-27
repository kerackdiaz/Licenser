package com._mas1r.licenser.dtos;

import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.UserCompany;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsersDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String role;

    public UsersDTO(UserCompany userCompany) {
        this.firstName = userCompany.getFirstName();
        this.lastName = userCompany.getLastName();
        this.username = userCompany.getUsername();
        this.email = userCompany.getEmail();
        this.role = userCompany.getRole();
    }

    public UsersDTO(AdminCompany adminCompany) {
        this.firstName = adminCompany.getFirstName();
        this.lastName = adminCompany.getLastName();
        this.username = adminCompany.getUsername();
        this.email = adminCompany.getEmail();
        this.role = adminCompany.getRole();
    }


}
