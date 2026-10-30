package com.neutreeno.footprint_review.controller;

import com.neutreeno.footprint_review.model.AppUser;
import com.neutreeno.footprint_review.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository repository;

    public AuthController(AppUserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        AppUser user = repository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public record UserResponse(Long id, String name, String email, String role) {
    }
}
