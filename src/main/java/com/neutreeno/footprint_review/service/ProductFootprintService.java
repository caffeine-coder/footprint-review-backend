package com.neutreeno.footprint_review.service;

import com.neutreeno.footprint_review.model.ProductFootPrint;
import com.neutreeno.footprint_review.model.ReviewStatus;
import com.neutreeno.footprint_review.repository.ProductFootprintRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductFootprintService {

    private final ProductFootprintRepository repository;

    public ProductFootprintService(ProductFootprintRepository repository) {
        this.repository = repository;
    }

    public List<ProductFootPrint> getAllFootprints() {
        return repository.findAll();
    }

    public ProductFootPrint getFootprintById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product footprint not found"
                ));
    }

    public ProductFootPrint reviewFootprint(
            Long id,
            ReviewStatus status,
            String comment
    ) {

        if (status != ReviewStatus.APPROVED &&
                status != ReviewStatus.REJECTED) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid review status"
            );
        }

        if (comment != null) {
            comment = comment.trim();
        }

        if (comment != null && comment.isEmpty()) {
            comment = null;
        }

        if (comment != null && comment.length() > 1000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Review comment cannot exceed 1000 characters"
            );
        }

        int updatedRows = repository.updateReview(id, status, comment);

        if (updatedRows == 0) {

            ProductFootPrint existing = getFootprintById(id);

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Submission has already been reviewed"
            );
        }

        return getFootprintById(id);
    }
}