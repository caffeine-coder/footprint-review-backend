
package com.neutreeno.footprint_review.controller;

import com.neutreeno.footprint_review.model.ProductFootPrint;
import com.neutreeno.footprint_review.model.ReviewStatus;
import com.neutreeno.footprint_review.service.ProductFootprintService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/footprints")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductFootprintController {

    private final ProductFootprintService service;

    public ProductFootprintController(ProductFootprintService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductFootPrint> getAllFootprints() {
        return service.getAllFootprints();
    }

    @GetMapping("/{id}")
    public ProductFootPrint getFootprintById(@PathVariable Long id) {
        return service.getFootprintById(id);
    }

    @PatchMapping("/{id}/review")
    public ProductFootPrint reviewFootprint(
            @PathVariable Long id,
            @RequestBody ReviewRequest request
    ) {
        return service.reviewFootprint(
                id,
                request.status(),
                request.comment()
        );
    }

    public record ReviewRequest(
            ReviewStatus status,
            String comment
    ) {
    }
}
