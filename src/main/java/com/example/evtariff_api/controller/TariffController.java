package com.example.evtariff_api.controller;

import java.time.LocalTime;
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

import com.example.evtariff_api.model.Tariff;
import com.example.evtariff_api.service.TariffService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/tariffs")
public class TariffController {

    private final TariffService tariffService;
    private final Auth_service authService;

    public TariffController(
            TariffService tariffService,
            Auth_service authService) {

        this.tariffService = tariffService;
        this.authService = authService;
    }


    // ADD TARIFF - ADMIN ONLY
    @PostMapping
    public Tariff addTariff(
            @RequestBody Tariff tariff,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return tariffService.addTariff(tariff);
    }


    // VIEW ALL TARIFFS - ADMIN + DRIVER
    @GetMapping
    public List<Tariff> getAllTariffs(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "DRIVER"
        );

        return tariffService.getAllTariffs();
    }


    // VIEW TARIFF BY ID - ADMIN + DRIVER
    @GetMapping("/{id}")
    public Tariff getTariffById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "DRIVER"
        );

        return tariffService.getTariffById(id);
    }


    // UPDATE TARIFF - ADMIN ONLY
    @PutMapping("/{id}")
    public Tariff updateTariff(
            @PathVariable Long id,
            @RequestBody Tariff tariff,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return tariffService.updateTariff(
                id,
                tariff
        );
    }


    // DELETE TARIFF - ADMIN ONLY
    @DeleteMapping("/{id}")
    public void deleteTariff(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        tariffService.deleteTariff(id);
    }


    // CALCULATE TARIFF RATE - ADMIN + DRIVER
    @GetMapping("/{id}/rate")
    public double calculateRate(
            @PathVariable Long id,
            @RequestParam String time,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "DRIVER"
        );

        LocalTime localTime =
                LocalTime.parse(time);

        return tariffService.calculateRate(
                id,
                localTime
        );
    }
}