package com.metehansargin.jwt.controller;

import com.metehansargin.jwt.dto.DtoUser;
import com.metehansargin.jwt.jwt.AuthRequest;
import com.metehansargin.jwt.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }
    @PostMapping("/register")
    public DtoUser save(@Valid @RequestBody AuthRequest authRequest){
        return registerService.save(authRequest);
    }

}
