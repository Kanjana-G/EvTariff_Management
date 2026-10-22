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

import com.example.evtariff_api.model.Budget;
import com.example.evtariff_api.service.BudgetService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final Auth_service authService;

    public BudgetController(
            BudgetService budgetService,
            Auth_service authService) {

        this.budgetService = budgetService;
        this.authService = authService;
    }


    // ADD BUDGET - ADMIN ONLY
    @PostMapping
    public Budget addBudget(
            @RequestBody Budget budget,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return budgetService.addBudget(budget);
    }


    // VIEW ALL BUDGETS - ADMIN + ACCOUNTANT
    @GetMapping
    public List<Budget> getAllBudgets(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return budgetService.getAllBudgets();
    }


    // VIEW BUDGET BY ID - ADMIN + ACCOUNTANT
    @GetMapping("/{id}")
    public Budget getBudgetById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return budgetService.getBudgetById(id);
    }


    // VIEW REMAINING BUDGET - ADMIN + ACCOUNTANT
    @GetMapping("/{id}/remaining")
    public Double getRemainingBudget(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return budgetService.getRemainingBudget(id);
    }


    // UPDATE BUDGET - ADMIN ONLY
    @PutMapping("/{id}")
    public Budget updateBudget(
            @PathVariable Long id,
            @RequestBody Budget budget,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return budgetService.updateBudget(
                id,
                budget
        );
    }


    // DELETE BUDGET - ADMIN ONLY
    @DeleteMapping("/{id}")
    public String deleteBudget(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN"
        );

        return budgetService.deleteBudget(id);
    }
}