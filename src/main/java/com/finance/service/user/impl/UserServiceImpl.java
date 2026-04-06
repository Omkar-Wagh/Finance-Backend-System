package com.finance.service.user.impl;

import com.finance.dto.UserDto;
import com.finance.dto.UserLoginRequest;
import com.finance.dto.UserRequestDto;
import com.finance.entity.User;
import com.finance.enums.Role;
import com.finance.exception.BadRequestException;
import com.finance.exception.UserNotFoundException;
import com.finance.mapper.UserMapper;
import com.finance.repo.UserRepository;
import com.finance.security.JWTService;
import com.finance.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder encoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDto register(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);

        boolean isPresent = userRepository.existsByEmail(user.getEmail());
        if(isPresent){
            throw new BadRequestException("email id already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        User createdUser = userRepository.save(user);
        jwtService.generateToken(user.getName());
        return userMapper.toDTO(createdUser);
    }

    @Override
    public String login(UserLoginRequest dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        return jwtService.generateToken(dto.getEmail());
    }

    @Override
    public UserDto update(UserDto dto) {
        User user = userRepository.findById(dto.getId()).orElse(null);
        if(user == null){
            throw new UserNotFoundException("user not found for Id : " + dto.getId());
        }
        user.setName(dto.getName());
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        user.setActive(dto.isActive());
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }
}
