package com.efecandonmez.subtracker.subscription;

import com.efecandonmez.subtracker.subscription.dto.SubscriptionRequest;
import com.efecandonmez.subtracker.subscription.dto.SubscriptionResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public SubscriptionResponse create(@Valid @RequestBody SubscriptionRequest request,
                                       Authentication authentication) {
        UUID userId = currentUserId(authentication);
        return subscriptionService.create(userId, request);
    }

    @GetMapping
    public List<SubscriptionResponse> list(Authentication authentication) {
        UUID userId = currentUserId(authentication);
        return subscriptionService.listForUser(userId);
    }

    @PutMapping("/{id}")
    public SubscriptionResponse update(@PathVariable UUID id,
                                       @Valid @RequestBody SubscriptionRequest request,
                                       Authentication authentication) {
        UUID userId = currentUserId(authentication);
        return subscriptionService.update(userId, id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, Authentication authentication) {
        UUID userId = currentUserId(authentication);
        subscriptionService.delete(userId, id);
    }

    private UUID currentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}