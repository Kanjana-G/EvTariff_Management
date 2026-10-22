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

import com.example.evtariff_api.model.Invoice;
import com.example.evtariff_api.service.InvoiceService;
import com.example.evtariff_api.service.Auth_service;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/ev/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final Auth_service authService;

    public InvoiceController(
            InvoiceService invoiceService,
            Auth_service authService) {

        this.invoiceService = invoiceService;
        this.authService = authService;
    }


    // CREATE INVOICE - ACCOUNTANT ONLY
    @PostMapping("/session/{sessionId}")
    public Invoice createInvoice(
            @PathVariable Long sessionId,
            HttpSession session) {

        authService.requireRole(
                session,
                "ACCOUNTANT"
        );

        return invoiceService.createInvoice(sessionId);
    }


    // VIEW ALL INVOICES - ADMIN + ACCOUNTANT
    @GetMapping
    public List<Invoice> getAllInvoices(
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return invoiceService.getAllInvoices();
    }


    // VIEW INVOICE BY ID - ADMIN + ACCOUNTANT + DRIVER
    @GetMapping("/{id}")
    public Invoice getInvoiceById(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT",
                "DRIVER"
        );

        return invoiceService.getInvoiceById(id);
    }


    // UPDATE INVOICE - ACCOUNTANT ONLY
    @PutMapping("/{id}")
    public Invoice updateInvoice(
            @PathVariable Long id,
            @RequestBody Invoice invoice,
            HttpSession session) {

        authService.requireRole(
                session,
                "ACCOUNTANT"
        );

        return invoiceService.updateInvoice(
                id,
                invoice
        );
    }


    // DELETE INVOICE - ADMIN + ACCOUNTANT
    @DeleteMapping("/{id}")
    public String deleteInvoice(
            @PathVariable Long id,
            HttpSession session) {

        authService.requireRole(
                session,
                "ADMIN",
                "ACCOUNTANT"
        );

        return invoiceService.deleteInvoice(id);
    }
}