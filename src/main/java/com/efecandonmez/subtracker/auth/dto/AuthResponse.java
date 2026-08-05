package com.efecandonmez.subtracker.auth.dto;

public record AuthResponse(String token, long expiresAt) {}