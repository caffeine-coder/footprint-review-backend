package com.neutreeno.footprint_review.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("demo")
public class DemoUserSeeder {

    @Bean
    public ApplicationRunner seedUsers(
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder,
            @Value("${DEMO_PASSWORD:}") String demoPassword
    ) {
        return args -> {
            if (demoPassword.isBlank()) {
                throw new IllegalStateException("DEMO_PASSWORD is required");
            }

            createUser(jdbcTemplate, passwordEncoder,
                    "Demo Admin", "admin@demo.local", "ADMIN", demoPassword);

            createUser(jdbcTemplate, passwordEncoder,
                    "Demo Editor", "editor@demo.local", "EDITOR", demoPassword);

            createUser(jdbcTemplate, passwordEncoder,
                    "Demo Viewer", "viewer@demo.local", "VIEWER", demoPassword);
        };
    }

    private void createUser(
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder,
            String name,
            String email,
            String role,
            String password
    ) {
        String sql = """
                INSERT INTO users (name, email, password_hash, role, enabled, created_at)
                VALUES (?, ?, ?, ?, TRUE, CURRENT_TIMESTAMP)
                ON CONFLICT (email) DO NOTHING
                """;

        jdbcTemplate.update(sql,
                name,
                email,
                passwordEncoder.encode(password),
                role
        );
    }
}
