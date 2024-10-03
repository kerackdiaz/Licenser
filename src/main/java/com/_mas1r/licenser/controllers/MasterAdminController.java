package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.service.MasterAdminService;
import com._mas1r.licenser.service.impl.MasterAdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/master-admin")
public class MasterAdminController {

    @Autowired
    private MasterAdminService masterAdminService;

    @PostMapping("/generate-keys/{id}")
    public String generateKeys() {
        try {
            masterAdminService.generateAndStoreKeys();
            return "Keys generated successfully";
        } catch (NoSuchAlgorithmException e) {
            return "Error generating keys: " + e.getMessage();
        }
    }


    @GetMapping("/getallcompanies")
    public ResponseEntity<List<CompanyExtractDTO>> getAllCompanies(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            List<CompanyExtractDTO> companies = masterAdminService.getAllCompanies(publicKey);
            if (companies != null) {
                return ResponseEntity.ok(companies);
            }
        }
        return ResponseEntity.status(403).body(null);
    }

    @PostMapping("/createnewcompany")
    public ResponseEntity<Map<String, Object>> createNewCompany(@RequestHeader("Authorization") String authorizationHeader, @RequestBody RegisterCompanyDTO registerCompanyDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            Map<String, Object> response = masterAdminService.createNewCompany(publicKey, registerCompanyDTO);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<Map<String, Object>> changeStatusCompany(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            Map<String, Object> response = masterAdminService.changeStatusCompany(publicKey, id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
    }

    @PostMapping("/renewal-license/{id}")
    public ResponseEntity<Map<String, Object>> renewalLicense(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id, @RequestBody LicenseCompanyStatus licenseStatus) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            Map<String, Object> response = masterAdminService.renewalLicense(publicKey, id, licenseStatus);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
    }

}