package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.CompanyDTO;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.dtos.SignUpDTO;
import com._mas1r.licenser.models.AdminCompany;
import com._mas1r.licenser.models.Company;
import com._mas1r.licenser.models.UserCompany;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.service.AuthenticacionService;
import com._mas1r.licenser.service.CompanyService;
import com._mas1r.licenser.service.ProjectService;
import com._mas1r.licenser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vi/company")
public class CompanyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CompanyService companyService;



    @Operation(summary = "Get Agency Data", description = "Devuelve toda la info de la agencia/Empresa")
    @GetMapping("/Current")
//    @Hidden
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getCurrentData(){
    return ResponseEntity.ok(companyService.currentCompany());
    }



    @Operation(summary = "Get All Agencies", description = "Devuelve todas las Agencias/Empresas")
    @GetMapping("/agencies")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getAllAgencies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }



    @Operation(summary = "Update Company", description = "Actualiza la informacion de la empresa")
    @PutMapping("/update/{companyId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> updateCompany(@PathVariable String companyId, @RequestBody CompanyDTO company){
        return ResponseEntity.ok(companyService.updateCompany(companyId, company));
    }



    @Operation(summary = "Create Company", description = "Crea una nueva empresa")
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> createCompany(@RequestBody RegisterCompanyDTO register){
        return ResponseEntity.ok(companyService.CreateCompany(register));
    }

}
