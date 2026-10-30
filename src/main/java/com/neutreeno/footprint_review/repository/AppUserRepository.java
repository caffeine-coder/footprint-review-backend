package com.neutreeno.footprint_review.repository;

import com.neutreeno.footprint_review.model.AppUser;
import com.neutreeno.footprint_review.model.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<AppUser> findByEmail(String email) {
        String sql = """
                SELECT * FROM users
                WHERE LOWER(email) = LOWER(?)
                """;

        List<AppUser> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            AppUser user = new AppUser();

            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setRole(Role.valueOf(rs.getString("role")));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setCreatedAt(rs.getTimestamp("created_at").toInstant());

            return user;
        }, email);

        return users.stream().findFirst();
    }
}
