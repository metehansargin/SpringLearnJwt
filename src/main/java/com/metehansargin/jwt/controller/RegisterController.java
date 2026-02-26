package com.metehansargin.jwt.controller;

import com.metehansargin.jwt.dto.DtoUser;
import com.metehansargin.jwt.jwt.AuthRequest;
import com.metehansargin.jwt.jwt.AuthResponse;
import com.metehansargin.jwt.service.AuthorizeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final AuthorizeService authorizeService;

    public RegisterController(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;
    }

    @PostMapping("/register")
    public DtoUser saveRegister(@Valid @RequestBody AuthRequest authRequest){
        return authorizeService.register(authRequest);
    }

    @PostMapping("/authenticate")
    public AuthResponse saveAuthentice(@Valid @RequestBody AuthRequest authRequest){
        return authorizeService.authenticate(authRequest);
    }

}
