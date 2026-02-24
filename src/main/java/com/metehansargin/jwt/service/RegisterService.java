package com.metehansargin.jwt.service;

import com.metehansargin.jwt.dto.DtoUser;
import com.metehansargin.jwt.entity.User;
import com.metehansargin.jwt.jwt.AuthRequest;
import com.metehansargin.jwt.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegisterService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public DtoUser save(AuthRequest authRequest){
        DtoUser dto=new DtoUser();
        User user=new User();

        user.setUsername(authRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(authRequest.getPassword()));

        User savedUser=userRepository.save(user);
        BeanUtils.copyProperties(savedUser,dto);

        return dto;

    }

}
