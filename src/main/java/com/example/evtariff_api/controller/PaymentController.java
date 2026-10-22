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

import com.example.evtariff_api.model.Payment;
import com.example.evtariff_api.service.PaymentService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final Auth_service authService;

    public PaymentController(
            PaymentService paymentService,
            Auth_service authService) {

        this.paymentService = paymentService;
        this.authService = authService;
    }


    // MAKE PAYMENT - ACCOUNTANT ONLY
    @PostMapping("/invoice/{invoiceId}")
    public Payment makePayment(
            @PathVariable Long invoiceId,
            @RequestParam String paymentMethod,
            HttpSession session) {

        authService.requireRole(
                session,
                "ACCOUNTANT"
        );

        return paymentService.makePayment(
                invoiceId,
                paymentMethod
        );
    }


    // VIEW ALL PAYMENTS - ADMIN + ACCOUNTANT
    @GetMapping
    public List<Payment> getAllPayments(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return paymentService.getAllPayments();
    }


    // VIEW PAYMENT BY ID - ADMIN + ACCOUNTANT
    @GetMapping("/{id}")
    public Payment getPaymentById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return paymentService.getPaymentById(id);
    }


    // UPDATE PAYMENT - ACCOUNTANT ONLY
    @PutMapping("/{id}")
    public Payment updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment,
            HttpSession session) {

        authService.requireRole(
                session,
                "ACCOUNTANT"
        );

        return paymentService.updatePayment(
                id,
                payment
        );
    }


    // DELETE PAYMENT - ADMIN + ACCOUNTANT
    @DeleteMapping("/{id}")
    public String deletePayment(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return paymentService.deletePayment(id);
    }
}