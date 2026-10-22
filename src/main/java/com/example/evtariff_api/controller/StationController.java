package com.example.evtariff_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.service.StationService;
import com.example.evtariff_api.service.ReportService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/stations")
public class StationController {

    private final StationService stationService;
    private final ReportService reportService;
    private final Auth_service authService;

    public StationController(
            StationService stationService,
            ReportService reportService,
            Auth_service authService) {

        this.stationService = stationService;
        this.reportService = reportService;
        this.authService = authService;
    }


    // ADD STATION - ADMIN ONLY
    @PostMapping
    public Station addStation(
            @RequestBody Station station,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return stationService.addStation(station);
    }


    // VIEW ALL STATIONS - ALL LOGGED-IN ROLES
    @GetMapping
    public List<Station> getAllStations(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT",
                "DRIVER"
        );

        return stationService.getAllStations();
    }


    // VIEW STATION BY ID - ALL LOGGED-IN ROLES
    @GetMapping("/{id}")
    public Station getStationById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT",
                "DRIVER"
        );

        return stationService.getStationById(id);
    }


    // UPDATE STATION - ADMIN ONLY
    @PutMapping("/{id}")
    public Station updateStation(
            @PathVariable Long id,
            @RequestBody Station station,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return stationService.updateStation(
                id,
                station
        );
    }


    // DELETE STATION - ADMIN ONLY
    @DeleteMapping("/{id}")
    public void deleteStation(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        stationService.deleteStation(id);
    }


    // VIEW STATION YIELD - ADMIN + ACCOUNTANT
    @GetMapping("/{id}/yield")
    public ResponseEntity<Map<String, Object>>
    getStationYield(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        Map<String, Object> report =
                reportService.getSiteYield(id);

        if (report == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(report);
    }
}