package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.MailBodyDTO;
import com._mas1r.licenser.models.EmailBody;
import com._mas1r.licenser.service.MailSenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
public class EmailBuilderController {

    @Autowired
    private MailSenderService mailSenderService;

    @Operation(summary = "Create Email", description = "Crea un email")
    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> createEmail(@RequestBody MailBodyDTO mailBodyDTO){
        return ResponseEntity.ok(mailSenderService.createMailBody(mailBodyDTO));
    }

    @Operation(summary = "Get All Emails", description = "Devuelve todos los emails")
    @GetMapping("/all")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getAllEmails(){
        return ResponseEntity.ok(mailSenderService.getAllEmails());
    }

    @Operation(summary = "Update Email", description = "Actualiza un email")
    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public String updateEmail(@RequestBody MailBodyDTO mailBodyDTO){
        return mailSenderService.updateEmail(mailBodyDTO);
    }

    @Operation(summary = "Delete Email", description = "Elimina un email")
    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    public String deleteEmail(@RequestParam UUID id){
        return mailSenderService.deleteEmail(id);
    }
}
