package com.efecandonmez.subtracker.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record FcmTokenRequest(@NotBlank String fcmToken) {}