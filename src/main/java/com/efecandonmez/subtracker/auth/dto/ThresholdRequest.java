package com.efecandonmez.subtracker.auth.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ThresholdRequest(@NotNull @DecimalMin("0.1") Double threshold) {}