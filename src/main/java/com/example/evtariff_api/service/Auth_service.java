package com.example.evtariff_api.service;

import com.example.evtariff_api.model.User;
import com.example.evtariff_api.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class Auth_service {

    private final UserRepository userRepository;

    public Auth_service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Generate a random salt
    public String generateSalt() {

        byte[] salt = new byte[16];

        new SecureRandom().nextBytes(salt);

        return Base64.getEncoder()
                .encodeToString(salt);
    }

    // SHA-256(password + salt)
    public String hashPassword(
            String password,
            String salt) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            String value = password + salt;

            byte[] hash = digest.digest(
                    value.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to hash password",
                    e
            );
        }
    }

    // Check username and password
    public User authenticate(
            String username,
            String password) {

        Optional<User> optionalUser =
                userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        String enteredPasswordHash =
                hashPassword(
                        password,
                        user.getSalt()
                );

        if (enteredPasswordHash.equals(
                user.getPassword())) {

            return user;
        }

        return null;
    }

    // Is someone logged in?
    public boolean isLoggedIn(
            HttpSession session) {

        return session.getAttribute(
                "username") != null;
    }

    // Check one role
    public boolean hasRole(
            HttpSession session,
            String role) {

        Object currentRole =
                session.getAttribute("role");

        return currentRole != null
                &&
                currentRole.toString()
                        .equalsIgnoreCase(role);
    }

    // Check multiple roles
    public boolean hasAnyRole(
            HttpSession session,
            String... roles) {

        Object currentRole =
                session.getAttribute("role");

        if (currentRole == null) {
            return false;
        }

        for (String role : roles) {

            if (currentRole.toString()
                    .equalsIgnoreCase(role)) {

                return true;
            }
        }

        return false;
    }
    public void requireLogin(HttpSession session) {

    if (!isLoggedIn(session)) {
        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Please login first"
        );
    }
}

public void requireRole(
        HttpSession session,
        String... allowedRoles) {

    requireLogin(session);

    if (!hasAnyRole(session, allowedRoles)) {
        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You do not have permission"
        );
    }
}
}