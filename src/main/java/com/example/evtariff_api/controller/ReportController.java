package com.example.evtariff_api.controller;

import com.example.evtariff_api.service.ReportService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ev/reports")
public class ReportController {

    private final ReportService reportService;
    private final Auth_service authService;


    public ReportController(
            ReportService reportService,
            Auth_service authService) {

        this.reportService = reportService;
        this.authService = authService;
    }


    /* PROFIT & LOSS */

    @GetMapping("/profit-loss")
    public ResponseEntity<Map<String, Object>>
    getProfitAndLoss(HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return ResponseEntity.ok(
                reportService.getProfitAndLoss()
        );
    }


    /* SITE YIELD */

    @GetMapping("/site-yield/{stationId}")
    public ResponseEntity<Map<String, Object>>
    getSiteYield(
            @PathVariable Long stationId,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        Map<String, Object> report =
                reportService.getSiteYield(stationId);


        if (report == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        return ResponseEntity.ok(report);
    }


    /* BUDGET REPORT */

    @GetMapping("/budget/{budgetId}")
    public ResponseEntity<Map<String, Object>>
    getBudgetReport(
            @PathVariable Long budgetId,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        Map<String, Object> report =
                reportService.getBudgetReport(budgetId);


        if (report == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        return ResponseEntity.ok(report);
    }
}