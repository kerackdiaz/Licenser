package com._mas1r.licenser.controllers;

import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.service.LicenseService;
import com._mas1r.licenser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    public Boolean checkLicense(@PathVariable String url) {
        return licenseService.checkLicense(url);
    }

}
