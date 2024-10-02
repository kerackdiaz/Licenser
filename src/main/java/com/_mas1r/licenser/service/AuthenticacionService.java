package com._mas1r.licenser.service;

import com._mas1r.licenser.dtos.RegisterCompanyDTO;
import com._mas1r.licenser.dtos.SignInDTO;
import com._mas1r.licenser.dtos.SignUpDTO;


import java.util.Map;
import java.util.UUID;


public interface AuthenticacionService {


    Map<String, Object> Register(SignUpDTO signUpDTO, String email);

    Map<String, Object> Login(SignInDTO signInDTO);


    String recoveryPassword(UUID id, String password);
}
