package com.neutreeno.footprint_review.repository;

import com.neutreeno.footprint_review.dto.ReviewHistoryItem;
import com.neutreeno.footprint_review.model.ReviewAction;
import com.neutreeno.footprint_review.model.ReviewStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewEventRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long footprintId, Long userId, ReviewStatus action, String comment) {
        String sql = """
                INSERT INTO review_events (footprint_id, user_id, action, comment, created_at)
                VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;

        jdbcTemplate.update(sql, footprintId, userId, action.name(), comment);
    }

    public List<ReviewHistoryItem> findByFootprintId(Long footprintId) {
        String sql = """
                SELECT e.id, e.action, e.comment, e.created_at, u.name AS reviewer_name
                FROM review_events e
                JOIN users u ON e.user_id = u.id
                WHERE e.footprint_id = ?
                ORDER BY e.created_at DESC, e.id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ReviewHistoryItem(
                        rs.getLong("id"),
                        ReviewAction.valueOf(rs.getString("action")),
                        rs.getString("comment"),
                        rs.getString("reviewer_name"),
                        rs.getTimestamp("created_at").toInstant()
                ), footprintId);
    }
}
