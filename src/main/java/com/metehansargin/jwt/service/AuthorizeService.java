package com.metehansargin.jwt.service;

import com.metehansargin.jwt.dto.DtoUser;
import com.metehansargin.jwt.entity.User;
import com.metehansargin.jwt.exception.BaseException;
import com.metehansargin.jwt.exception.ErrorMessage;
import com.metehansargin.jwt.exception.MessageType;
import com.metehansargin.jwt.jwt.AuthRequest;
import com.metehansargin.jwt.jwt.AuthResponse;
import com.metehansargin.jwt.jwt.JwtService;
import com.metehansargin.jwt.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizeService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public AuthorizeService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationProvider authenticationProvider, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    public DtoUser register(AuthRequest authRequest){
        DtoUser dto=new DtoUser();
        User user=new User();

        user.setUsername(authRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(authRequest.getPassword()));

        User savedUser=userRepository.save(user);
        BeanUtils.copyProperties(savedUser,dto);

        return dto;
    }
    public AuthResponse authenticate(AuthRequest authRequest){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword());

            authenticationProvider.authenticate(usernamePasswordAuthenticationToken);

            Optional<User> optinal =userRepository.findByUsername(authRequest.getUsername());

            String token=jwtService.generateToken(optinal.get());

            return new AuthResponse(token);
        }catch (Exception e){
           throw new BaseException(new ErrorMessage(MessageType.NO_USER,e.getMessage()));
        }
    }

}
