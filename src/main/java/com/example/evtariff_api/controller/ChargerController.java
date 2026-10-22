package com.example.evtariff_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evtariff_api.model.Charger;
import com.example.evtariff_api.service.ChargerService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/chargers")
public class ChargerController {

    private final ChargerService chargerService;
    private final Auth_service authService;

    public ChargerController(
            ChargerService chargerService,
            Auth_service authService) {

        this.chargerService = chargerService;
        this.authService = authService;
    }


    // ADD CHARGER - ADMIN ONLY
    @PostMapping
    public Charger addCharger(
            @RequestBody Charger charger,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return chargerService.addCharger(charger);
    }


    // VIEW ALL CHARGERS - ADMIN + DRIVER
    @GetMapping
    public List<Charger> getAllChargers(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "DRIVER"
        );

        return chargerService.getAllChargers();
    }


    // VIEW CHARGER BY ID - ADMIN + DRIVER
    @GetMapping("/{id}")
    public Charger getChargerById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "DRIVER"
        );

        return chargerService.getChargerById(id);
    }


    // UPDATE CHARGER - ADMIN ONLY
    @PutMapping("/{id}")
    public Charger updateCharger(
            @PathVariable Long id,
            @RequestBody Charger charger,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return chargerService.updateCharger(
                id,
                charger
        );
    }


    // DELETE CHARGER - ADMIN ONLY
    @DeleteMapping("/{id}")
    public void deleteCharger(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        chargerService.deleteCharger(id);
    }
}