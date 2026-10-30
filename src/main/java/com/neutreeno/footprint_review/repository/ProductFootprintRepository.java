
package com.neutreeno.footprint_review.repository;

import com.neutreeno.footprint_review.model.ProductFootPrint;
import com.neutreeno.footprint_review.model.ReviewStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductFootprintRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductFootprintRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<ProductFootPrint> rowMapper = (rs, rowNum) -> {

        ProductFootPrint footprint = new ProductFootPrint();

        footprint.setId(rs.getLong("id"));
        footprint.setProductName(rs.getString("product_name"));
        footprint.setSupplierName(rs.getString("supplier_name"));
        footprint.setEmissionsKgCo2e(rs.getBigDecimal("emissions_kg_co2e"));
        footprint.setUncertaintyPercent(rs.getBigDecimal("uncertainty_percent"));
        footprint.setStatus(ReviewStatus.valueOf(rs.getString("status")));
        footprint.setReviewComment(rs.getString("review_comment"));

        Timestamp submittedAt = rs.getTimestamp("submitted_at");
        if (submittedAt != null) {
            footprint.setSubmittedAt(submittedAt.toInstant());
        }

        Timestamp reviewedAt = rs.getTimestamp("reviewed_at");
        if (reviewedAt != null) {
            footprint.setReviewedAt(reviewedAt.toInstant());
        }

        return footprint;
    };


    public List<ProductFootPrint> findAll(int page, int size, String status, String search) {
        String sql = """
            SELECT * FROM product_footprints
            WHERE (? = 'ALL' OR status = ?)
            AND (LOWER(product_name) LIKE ? OR LOWER(supplier_name) LIKE ?)
            ORDER BY submitted_at DESC, id DESC
            LIMIT ? OFFSET ?
            """;

        String searchText = "%" + search.toLowerCase() + "%";

        return jdbcTemplate.query(sql, rowMapper,
                status, status, searchText, searchText, size, page * size);
    }

    public long countAll(String status, String search) {
        String sql = """
            SELECT COUNT(*) FROM product_footprints
            WHERE (? = 'ALL' OR status = ?)
            AND (LOWER(product_name) LIKE ? OR LOWER(supplier_name) LIKE ?)
            """;

        String searchText = "%" + search.toLowerCase() + "%";

        return jdbcTemplate.queryForObject(sql, Long.class, status, status, searchText, searchText);
    }


    public Optional<ProductFootPrint> findById(Long id) {

        String sql = """
                SELECT * FROM product_footprints
                WHERE id = ?
                """;

        List<ProductFootPrint> result = jdbcTemplate.query(
                sql,
                rowMapper,
                id
        );

        return result.stream().findFirst();
    }

    public int updateReview(
            Long id,
            ReviewStatus status,
            String comment
    ) {

        String sql = """
                UPDATE product_footprints
                SET status = ?,
                    review_comment = ?,
                    reviewed_at = CURRENT_TIMESTAMP
                WHERE id = ?
                AND status = 'PENDING'
                """;

        return jdbcTemplate.update(
                sql,
                status.name(),
                comment,
                id
        );
    }
}
