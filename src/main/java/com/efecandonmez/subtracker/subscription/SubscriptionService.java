package com.efecandonmez.subtracker.subscription;

import com.efecandonmez.subtracker.common.exception.ResourceNotFoundException;
import com.efecandonmez.subtracker.subscription.dto.SubscriptionRequest;
import com.efecandonmez.subtracker.subscription.dto.SubscriptionResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public SubscriptionService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public SubscriptionResponse create(UUID userId, SubscriptionRequest request) {
        Subscription sub = new Subscription(
                userId,
                request.name(),
                request.price(),
                request.currency().toUpperCase(),
                request.billingCycle(),
                request.nextPaymentDate(),
                request.category()
        );
        return toResponse(repository.save(sub));
    }

    public List<SubscriptionResponse> listForUser(UUID userId) {
        return repository.findByUserIdAndActiveTrue(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public SubscriptionResponse update(UUID userId, UUID subscriptionId, SubscriptionRequest request) {
        Subscription sub = getOwned(userId, subscriptionId);

        sub.setName(request.name());
        sub.setPrice(request.price());
        sub.setCurrency(request.currency().toUpperCase());
        sub.setBillingCycle(request.billingCycle());
        sub.setNextPaymentDate(request.nextPaymentDate());
        sub.setCategory(request.category());

        return toResponse(repository.save(sub));
    }

    public void delete(UUID userId, UUID subscriptionId) {
        Subscription sub = getOwned(userId, subscriptionId);
        sub.setActive(false); // soft delete
        repository.save(sub);
    }

    private Subscription getOwned(UUID userId, UUID subscriptionId) {
        Subscription sub = repository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Abonelik bulunamadı"));

        if (!sub.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Abonelik bulunamadı");
        }
        return sub;
    }

    private SubscriptionResponse toResponse(Subscription sub) {
        return new SubscriptionResponse(
                sub.getId(), sub.getName(), sub.getPrice(), sub.getCurrency(),
                sub.getBillingCycle(), sub.getNextPaymentDate(), sub.getCategory(), sub.isActive()
        );
    }
}