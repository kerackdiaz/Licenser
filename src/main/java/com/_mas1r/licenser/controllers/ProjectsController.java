package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.ProjectDTO;
import com._mas1r.licenser.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectsController {

    @Autowired
    private ProjectService projectService;


    @Operation(summary = "Create Project", description = "Crea un nuevo proyecto")
    @PostMapping("/create")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO project) throws MessagingException {
        return ResponseEntity.ok(projectService.newProject(project));
    }



    @Operation(summary = "Get Projects", description = "Obtiene todos los proyectos")
    @PostMapping("/renew/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> renewProject(@PathVariable Long id) throws MessagingException {
        return ResponseEntity.ok(projectService.renewProject(id));
    }



    @Operation(summary = "Update Projects", description = "Actualiza un proyecto")
    @PatchMapping("/update/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDTO project) throws MessagingException {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }



    @Operation(summary = "Get Project", description = "Optiene un proyecto")
    @GetMapping("/project/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProject(id));
    }
}
