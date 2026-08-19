package com.efecandonmez.subtracker.auth;

import com.efecandonmez.subtracker.auth.dto.AuthResponse;
import com.efecandonmez.subtracker.auth.dto.LoginRequest;
import com.efecandonmez.subtracker.auth.dto.RegisterRequest;
import com.efecandonmez.subtracker.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.efecandonmez.subtracker.common.exception.DuplicateResourceException;
import com.efecandonmez.subtracker.common.exception.InvalidCredentialsException;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Bu email zaten kayıtlı");
        }

        User user = new User(request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return buildResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email veya şifre hatalı"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Email veya şifre hatalı");
        }

        return buildResponse(user);
    }

    public void updateFcmToken(UUID userId, String fcmToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
        user.setFcmToken(fcmToken);
        userRepository.save(user);
    }

    private AuthResponse buildResponse(User user) {
        String token = jwtService.generateToken(user);
        long expiresAt = System.currentTimeMillis() + jwtService.getExpirationMs();
        return new AuthResponse(token, expiresAt);
    }

    public void updateRateChangeThreshold(UUID userId, Double threshold) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
        user.setRateChangeThreshold(threshold);
        userRepository.save(user);
    }
}