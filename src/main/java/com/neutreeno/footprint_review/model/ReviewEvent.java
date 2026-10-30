package com.neutreeno.footprint_review.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "review_events")
public class ReviewEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footprint_id", nullable = false)
    private ProductFootPrint footprint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewAction action;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public ReviewEvent() {
    }

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductFootPrint getFootprint() { return footprint; }
    public void setFootprint(ProductFootPrint footprint) { this.footprint = footprint; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public ReviewAction getAction() { return action; }
    public void setAction(ReviewAction action) { this.action = action; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
