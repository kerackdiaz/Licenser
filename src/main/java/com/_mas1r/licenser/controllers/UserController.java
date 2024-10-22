package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.MasterAdminDTO;
import com._mas1r.licenser.dtos.MasterAdminUpdateDTO;
import com._mas1r.licenser.dtos.UsersDTO;
import com._mas1r.licenser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Get Users", description = "Devuelve todos los usuarios de la agencia/empresa")
    @GetMapping("/users")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @Operation(summary = "Update User", description = "Actualiza un usuario")
    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UsersDTO user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @Operation(summary = "Get Master", description = "Devuelve el Master Admin")
    @GetMapping("/current/master")
    @SecurityRequirement(name = "Bearer Authentication")
    public MasterAdminDTO currentMaster() {
        return new MasterAdminDTO(userService.currentMaster());
    }

    @Operation(summary = "Update Master", description = "Actualiza el Master Admin")
    @PatchMapping("/updateMaster")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> updateMaster(@RequestBody MasterAdminUpdateDTO master) {
        return ResponseEntity.ok(userService.updateMasterAdmin(master));
    }

    @Operation(summary = "Delete User", description = "Elimina un usuario")
    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}
