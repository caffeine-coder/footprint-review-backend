package com.neutreeno.footprint_review.controller;

import com.neutreeno.footprint_review.dto.ReviewHistoryItem;
import com.neutreeno.footprint_review.model.ProductFootPrint;
import com.neutreeno.footprint_review.model.ReviewStatus;
import com.neutreeno.footprint_review.service.ProductFootprintService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/footprints")
public class ProductFootprintController {

    private final ProductFootprintService service;

    public ProductFootprintController(ProductFootprintService service) {
        this.service = service;
    }

    @GetMapping
    public ProductFootprintService.PageResult getAllFootprints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "") String search) {
        return service.getAllFootprints(page, size, status, search.trim());
    }

    @GetMapping("/{id}")
    public ProductFootPrint getFootprintById(@PathVariable Long id) {
        return service.getFootprintById(id);
    }

    @GetMapping("/{id}/history")
    public List<ReviewHistoryItem> getReviewHistory(@PathVariable Long id) {
        return service.getReviewHistory(id);
    }

    @PatchMapping("/{id}/review")
    public ProductFootPrint reviewFootprint(
            @PathVariable Long id,
            @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        return service.reviewFootprint(
                id,
                request.status(),
                request.comment(),
                authentication.getName()
        );
    }

    public record ReviewRequest(
            ReviewStatus status,
            String comment
    ) {
    }
}
