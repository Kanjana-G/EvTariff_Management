package com.example.evtariff_api.controller;

import com.example.evtariff_api.model.User;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Auth_service authService;

    public AuthController(Auth_service authService) {
        this.authService = authService;
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> request,
            HttpSession session) {

        String username = request.get("username");
        String password = request.get("password");

        User user = authService.authenticate(
                username,
                password
        );

        if (user == null) {

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put("success", false);
            response.put(
                    "message",
                    "Invalid username or password"
            );

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        // Store login information in session
        session.setAttribute(
                "username",
                user.getUsername()
        );

        session.setAttribute(
                "role",
                user.getRole()
        );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("success", true);
        response.put(
                "username",
                user.getUsername()
        );
        response.put(
                "role",
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }


    // CURRENT LOGGED-IN USER
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(
            HttpSession session) {

        if (!authService.isLoggedIn(session)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "username",
                session.getAttribute("username")
        );

        response.put(
                "role",
                session.getAttribute("role")
        );

        return ResponseEntity.ok(response);
    }


    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            HttpSession session) {

        session.invalidate();

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("success", true);
        response.put(
                "message",
                "Logged out successfully"
        );

        return ResponseEntity.ok(response);
    }
}