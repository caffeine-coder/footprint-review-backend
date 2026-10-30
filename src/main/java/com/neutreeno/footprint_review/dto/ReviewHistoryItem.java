package com.neutreeno.footprint_review.dto;

import com.neutreeno.footprint_review.model.ReviewAction;
import java.time.Instant;

public record ReviewHistoryItem(
        Long id,
        ReviewAction action,
        String comment,
        String reviewerName,
        Instant createdAt
) {
}
