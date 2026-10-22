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

import com.example.evtariff_api.model.UtilityExpense;
import com.example.evtariff_api.service.UtilityExpenseService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/expenses")
public class UtilityExpenseController {

    private final UtilityExpenseService expenseService;
    private final Auth_service authService;

    public UtilityExpenseController(
            UtilityExpenseService expenseService,
            Auth_service authService) {

        this.expenseService = expenseService;
        this.authService = authService;
    }


    // ADD EXPENSE - ADMIN + ACCOUNTANT
    @PostMapping
    public UtilityExpense addExpense(
            @RequestBody UtilityExpense expense,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return expenseService.addExpense(expense);
    }


    // VIEW ALL EXPENSES - ADMIN + ACCOUNTANT
    @GetMapping
    public List<UtilityExpense> getAllExpenses(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return expenseService.getAllExpenses();
    }


    // VIEW EXPENSE BY ID - ADMIN + ACCOUNTANT
    @GetMapping("/{id}")
    public UtilityExpense getExpenseById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return expenseService.getExpenseById(id);
    }


    // UPDATE EXPENSE - ADMIN + ACCOUNTANT
    @PutMapping("/{id}")
    public UtilityExpense updateExpense(
            @PathVariable Long id,
            @RequestBody UtilityExpense expense,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return expenseService.updateExpense(
                id,
                expense
        );
    }


    // DELETE EXPENSE - ADMIN + ACCOUNTANT
    @DeleteMapping("/{id}")
    public String deleteExpense(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return expenseService.deleteExpense(id);
    }
}