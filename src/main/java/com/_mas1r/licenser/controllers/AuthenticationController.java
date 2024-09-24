package com._mas1r.licenser.controllers;

import com._mas1r.licenser.dtos.SignInDTO;
import com._mas1r.licenser.dtos.SignUpDTO;
import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.repositories.AdminRepository;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.repositories.UserRepository;
import com._mas1r.licenser.security.JwtUtilService;
import com._mas1r.licenser.service.AuthenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticacionService authenticacionService;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Operation(summary = "Sign in", description = "Sign in")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(authenticacionService.Register(signUpDTO,email));
    }

    @Operation(summary = "Sign up", description = "Sign up")
    @PostMapping("/signin")
    public ResponseEntity<?> signUp(@RequestBody SignInDTO signInDTO) {
        return ResponseEntity.ok(authenticacionService.Login(signInDTO));
    }
}