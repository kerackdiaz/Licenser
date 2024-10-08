package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.CompanyExtractDTO;
import com._mas1r.licenser.dtos.LicenseCompanyStatus;
import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.service.MasterAdminService;
import com._mas1r.licenser.service.impl.MasterAdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MasterAdminController.class);

    @Autowired
    private MasterAdminService masterAdminService;

    @Operation(summary = "Generate Keys", description = "Genera las llaves publicas y privadas")
    @PostMapping("/generate-keys/")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> generateKeys() {
            try {
                masterAdminService.generateAndStoreKeys();
                return ResponseEntity.ok("Keys generated successfully");
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.status(500).body("Error generating keys: " + e.getMessage());
            }
    }

    @Operation(summary = "Get All Companies", description = "Obtiene todas las empresas registradas")
    @GetMapping("/getallcompanies")
    @SecurityRequirement(name = "Public Key Authentication")
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

    @Operation(summary = "Create New Company", description = "Crea una nueva empresa")
    @PostMapping("/createnewcompany")
    @SecurityRequirement(name = "Public Key Authentication")
    public ResponseEntity<Map<String, Object>> createNewCompany(@RequestHeader("Authorization") String authorizationHeader, @RequestBody RegisterCompanyDTO registerCompanyDTO) {
        logger.info("Received request to create new company with Authorization header: {}", authorizationHeader);
        logger.info("RegisterCompanyDTO: {}", registerCompanyDTO);

        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            Map<String, Object> response = masterAdminService.createNewCompany(publicKey, registerCompanyDTO);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
    }



    @Operation(summary = "Change Status Company", description = "Cambia el estado de una empresa")
    @PutMapping("/changeStatus/{id}")
    @SecurityRequirement(name = "Public Key Authentication")
    public ResponseEntity<Map<String, Object>> changeStatusCompany(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            Map<String, Object> response = masterAdminService.changeStatusCompany(publicKey, id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
    }

    @Operation(summary = "Renewal License", description = "Renueva la licencia de una empresa")
    @PostMapping("/renewal-license/{id}")
    @SecurityRequirement(name = "Public Key Authentication")
    public ResponseEntity<Map<String, Object>> renewalLicense(@RequestHeader("Authorization") String authorizationHeader, @PathVariable UUID id, @RequestBody LicenseCompanyStatus licenseStatus) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Key ")) {
            String publicKey = authorizationHeader.substring(4);
            Map<String, Object> response = masterAdminService.renewalLicense(publicKey, id, licenseStatus);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
    }

}
