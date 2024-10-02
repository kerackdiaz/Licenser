package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/license")
public class LicenseController {


    @Autowired
    private LicenseService licenseService;

    @Autowired
    private UserService userService;


    @Operation(summary = "Check License", description = "Revisa el estatus de la licencia")
    @GetMapping("/check/{url}")
    @SecurityRequirement(name = "Request Header CompanyKey")
    public ResponseEntity<?> checkLicense(@PathVariable String url, @RequestHeader("CompanyID") String key) {
        return ResponseEntity.ok(licenseService.checkLicense(url, key));
    }



    @Operation(summary = "Update License Company", description = "Actualiza la compañia de la licencia")
    @PatchMapping("/update/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> updateLicenseCompany(@PathVariable String id, @RequestBody LicenseCompanyStatus licenseCompanyStatus) {
        return ResponseEntity.ok(licenseService.updateLicenseCompany(id, licenseCompanyStatus));
    }

}
