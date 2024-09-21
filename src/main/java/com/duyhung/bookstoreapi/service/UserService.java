package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.UserDto;
import com.duyhung.bookstoreapi.entity.*;
import com.duyhung.bookstoreapi.jwt.JwtService;
import com.duyhung.bookstoreapi.repository.PasswordResetTokenRepository;
import com.duyhung.bookstoreapi.repository.RoleRepository;
import com.duyhung.bookstoreapi.repository.UserRepository;
import com.duyhung.bookstoreapi.repository.VerifyCodeRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final ModelMapper modelMapper;
    private final VerifyCodeService verifyCodeService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final VerifyCodeRepository verifyCodeRepository;

    public String register(AuthRequest request) throws Exception {
        User userExisted = userRepository.findByEmail(request.getEmail());
        if (userExisted != null) {
            throw new Exception("Username is existed");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        Role role = roleRepository.findById(1L).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
        return "Register successfully";
    }


    public LoginResponse login(AuthRequest request, HttpServletResponse response) throws UsernameNotFoundException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        LoginResponse loginResponse = new LoginResponse();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = (User) authentication.getPrincipal();
            loginResponse.setUser(modelMapper.map(user, UserDto.class));
            String accessToken = jwtService.generateJwtToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());
            loginResponse.setJwtToken(accessToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            setCookie("accessToken", accessToken, 86400, response);
            setCookie("refreshToken", refreshToken, 604800, response);
        }

        return loginResponse;
    }

    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = jwtService.getCookieValue(request, "refreshToken");
            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));
            if (refreshToken != null
                    && jwtService.verifyToken(refreshToken)
                    && refreshToken.equals(user.getRefreshToken()
            )) {

                String newAccessToken = jwtService.generateJwtToken(jwtService.extractEmail(refreshToken));

                setCookie("accessToken", newAccessToken, 86400, response);

                return "OK";
            } else {
                throw new RuntimeException("Invalid RFT");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void saveAvatarUrl(String url, String userId) throws RuntimeException {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        findUser.setPhotoUrl(url);
        userRepository.save(findUser);
    }

    public String verification(String code, String userId) throws RuntimeException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (verifyCodeService.validateCode(code)) {
            PasswordResetToken token = new PasswordResetToken();
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(expirationTime);
            token.setUser(user);
            passwordResetTokenRepository.save(token);
            return "http://localhost:5173/user/" + userId + "/change-password?token=" + token.getToken();
        } else {
            throw new RuntimeException("Invalid verification code");
        }
    }

    public String resetPassword(String token, String newPassword) throws RuntimeException {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new RuntimeException("Invalid request token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
        return "Password successfully changed";
    }

    public UserDto saveUserInfo(UserDto userDto) throws RuntimeException {
        User user = userRepository.findById(userDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPhone(userDto.getPhone());
        user.setName(userDto.getName());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());
        userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }

    public LoginResponse oauth2Login(OidcUser oidcUser, HttpServletResponse response) {
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String phone = oidcUser.getPhoneNumber();
        String photoUrl = oidcUser.getPicture();
        String accessToken = jwtService.generateJwtToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);

        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new RuntimeException("User already exists");
        }
        user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPhone(phone);
        user.setPhotoUrl(photoUrl);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Set a random password
        user.setRole(roleRepository.findById(1L).orElseThrow());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // Set cookies
        setCookie("accessToken", accessToken, 86400, response);

        // Return response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(modelMapper.map(user, UserDto.class));
        loginResponse.setJwtToken(accessToken);
        return loginResponse;
    }

    private void setCookie(String name, String token, int expiry, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(expiry);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String saveOTP(String code) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setCode(code);
        verifyCode.setExpirationTime(expirationTime);
        verifyCodeRepository.save(verifyCode);
        return "OPT saved";
    }
}
