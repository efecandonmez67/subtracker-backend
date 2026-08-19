package com.efecandonmez.subtracker.auth;

import com.efecandonmez.subtracker.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PutMapping("/fcm-token")
    public void updateFcmToken(@Valid @RequestBody FcmTokenRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        authService.updateFcmToken(userId, request.fcmToken());
    }

    @PutMapping("/rate-threshold")
    public void updateRateThreshold(@Valid @RequestBody ThresholdRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        authService.updateRateChangeThreshold(userId, request.threshold());
    }

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication.getName();
    }


}