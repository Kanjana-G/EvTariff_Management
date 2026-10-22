package com.example.evtariff_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.evtariff_api.model.ChargingSession;
import com.example.evtariff_api.service.ChargingSessService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/sessions")
public class ChargingSessController {

    private final ChargingSessService sessionService;
    private final Auth_service authService;

    public ChargingSessController(
            ChargingSessService sessionService,
            Auth_service authService) {

        this.sessionService = sessionService;
        this.authService = authService;
    }


    // START CHARGING SESSION - DRIVER ONLY
    @PostMapping
    public ChargingSession startSession(
            @RequestBody ChargingSession chargingSession,
            HttpSession session) {

        authService.requireRole(
                session,
                "DRIVER"
        );

        return sessionService.startSession(
                chargingSession
        );
    }


    // VIEW ALL SESSIONS - ADMIN ONLY
    @GetMapping
public List<ChargingSession> getAllSessions(
        HttpSession session) {

    authService.requireRole(
        session,
        "ADMIN",
        "ACCOUNTANT",
        "DRIVER"
    );

    return sessionService.getAllSessions();
}


    // VIEW SESSION BY ID - ADMIN + DRIVER
    @GetMapping("/{id}")
    public ChargingSession getSessionById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "DRIVER"
        );

        return sessionService.getSessionById(id);
    }


    // UPDATE SESSION - DRIVER ONLY
    @PutMapping("/{id}")
    public ChargingSession updateSession(
            @PathVariable Long id,
            @RequestBody ChargingSession chargingSession,
            HttpSession session) {

        authService.requireRole(
                session,
                "DRIVER"
        );

        return sessionService.updateSession(
                id,
                chargingSession
        );
    }


    // STOP CHARGING SESSION - DRIVER ONLY
    @PutMapping("/{id}/stop")
    public ChargingSession stopSession(
            @PathVariable Long id,
            @RequestParam double energyConsumed,
            HttpSession session) {

        authService.requireRole(
                session,
                "DRIVER"
        );

        return sessionService.stopSession(
                id,
                energyConsumed
        );
    }


    // DELETE SESSION - ADMIN ONLY
    @DeleteMapping("/{id}")
    public String deleteSession(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return sessionService.deleteSession(id);
    }
}