package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.UserDto;
import com.duyhung.bookstoreapi.entity.AuthRequest;
import com.duyhung.bookstoreapi.entity.LoginResponse;
import com.duyhung.bookstoreapi.entity.Role;
import com.duyhung.bookstoreapi.entity.User;
import com.duyhung.bookstoreapi.jwt.JwtService;
import com.duyhung.bookstoreapi.repository.RoleRepository;
import com.duyhung.bookstoreapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final ModelMapper modelMapper;


    public String register(AuthRequest request) throws Exception {
        User userExisted = userRepository.findByEmail(request.getEmail());
        if (userExisted != null) {
            throw new Exception("Username is existed");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findById(1L).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
        return "Register successfully";
    }


    public LoginResponse login(AuthRequest request) throws UsernameNotFoundException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        LoginResponse response = new LoginResponse();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User userDetail = (User) authentication.getPrincipal();
            response.setUser(modelMapper.map(userDetail, UserDto.class));
            response.setJwtToken(jwtService.generateJwtToken(userDetail));
        }
        return response;
    }

}
