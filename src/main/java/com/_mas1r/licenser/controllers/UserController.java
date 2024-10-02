package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.MasterAdminDTO;
import com._mas1r.licenser.dtos.UsersDTO;
import com._mas1r.licenser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PatchMapping("/updateMaster")
    public ResponseEntity<?> updateMaster(@RequestBody MasterAdminDTO master) {
        return ResponseEntity.ok(userService.updateMasterAdmin(master));
    }
}
