package com.efecandonmez.subtracker.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Setter
    private String fcmToken;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Setter
    @Column(nullable = false)
    private Double rateChangeThreshold = 2.0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }
}