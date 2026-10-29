package com.neutreeno.footprint_review.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product_footprints")
public class ProductFootPrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal emissionsKgCo2e;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal uncertaintyPercent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.PENDING;

    @Column(length = 1000)
    private String reviewComment;

    @Column(nullable = false, updatable = false)
    private Instant submittedAt;

    private Instant reviewedAt;

    public ProductFootPrint() {
    }

    @PrePersist
    public void onCreate() {
        if (submittedAt == null) {
            submittedAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getEmissionsKgCo2e() {
        return emissionsKgCo2e;
    }

    public void setEmissionsKgCo2e(BigDecimal emissionsKgCo2e) {
        this.emissionsKgCo2e = emissionsKgCo2e;
    }

    public BigDecimal getUncertaintyPercent() {
        return uncertaintyPercent;
    }

    public void setUncertaintyPercent(BigDecimal uncertaintyPercent) {
        this.uncertaintyPercent = uncertaintyPercent;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Instant reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
