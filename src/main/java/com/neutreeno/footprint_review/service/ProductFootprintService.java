package com.neutreeno.footprint_review.service;

import com.neutreeno.footprint_review.dto.ReviewHistoryItem;
import com.neutreeno.footprint_review.model.AppUser;
import com.neutreeno.footprint_review.model.ProductFootPrint;
import com.neutreeno.footprint_review.model.ReviewStatus;
import com.neutreeno.footprint_review.model.Role;
import com.neutreeno.footprint_review.repository.AppUserRepository;
import com.neutreeno.footprint_review.repository.ProductFootprintRepository;
import com.neutreeno.footprint_review.repository.ReviewEventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductFootprintService {

    private final ProductFootprintRepository repository;
    private final AppUserRepository userRepository;
    private final ReviewEventRepository reviewEventRepository;

    public ProductFootprintService(
            ProductFootprintRepository repository,
            AppUserRepository userRepository,
            ReviewEventRepository reviewEventRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.reviewEventRepository = reviewEventRepository;
    }

    public PageResult getAllFootprints(int page, int size, String status, String search) {
        page = Math.max(0, page);
        size = 10;

        if (!status.equals("ALL") && !status.equals("PENDING")
                && !status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
        }

        List<ProductFootPrint> items = repository.findAll(page, size, status, search);
        long total = repository.countAll(status, search);

        return new PageResult(items, total, page, size);
    }

    public record PageResult(List<ProductFootPrint> items, long total, int page, int size) {}

    public ProductFootPrint getFootprintById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product footprint not found"
                ));
    }

    public List<ReviewHistoryItem> getReviewHistory(Long id) {
        getFootprintById(id);
        return reviewEventRepository.findByFootprintId(id);
    }

    @Transactional
    public ProductFootPrint reviewFootprint(
            Long id,
            ReviewStatus status,
            String comment,
            String reviewerEmail
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

        AppUser user = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));

        if (!user.isEnabled() ||
                (user.getRole() != Role.ADMIN && user.getRole() != Role.EDITOR)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You do not have permission to review submissions"
            );
        }

        int updatedRows = repository.updateReview(id, status, comment);

        if (updatedRows == 0) {
            getFootprintById(id);

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Submission has already been reviewed"
            );
        }

        reviewEventRepository.save(id, user.getId(), status, comment);

        return getFootprintById(id);
    }
}
