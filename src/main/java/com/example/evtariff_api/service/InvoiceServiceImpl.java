package com.example.evtariff_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.ChargingSession;
import com.example.evtariff_api.model.Invoice;
import com.example.evtariff_api.repo.ChargingSessRepository;
import com.example.evtariff_api.repo.InvoiceRepository;

@Service
public class InvoiceServiceImpl
        implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ChargingSessRepository sessionRepository;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            ChargingSessRepository sessionRepository) {

        this.invoiceRepository = invoiceRepository;
        this.sessionRepository = sessionRepository;
    }

    // CREATE INVOICE
    @Override
    public Invoice createInvoice(Long sessionId) {

        ChargingSession session =
                sessionRepository.findById(sessionId)
                        .orElse(null);

        // Session must exist
        if (session == null) {
            return null;
        }

        // Invoice can be created only after charging ends
        if (!"COMPLETED".equalsIgnoreCase(
                session.getStatus())) {
            return null;
        }

        // Prevent duplicate invoice
        if (invoiceRepository
                .existsByChargingSessionId(sessionId)) {

            return null;
        }

        Invoice invoice = new Invoice();

        invoice.setChargingSession(session);

        invoice.setInvoiceDate(
                LocalDateTime.now());

        // Amount comes automatically from session
        invoice.setAmount(
                session.getTotalAmount());

        invoice.setPaymentStatus("UNPAID");

        return invoiceRepository.save(invoice);
    }

    // GET ALL INVOICES
    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // GET INVOICE BY ID
    @Override
    public Invoice getInvoiceById(Long id) {

        return invoiceRepository
                .findById(id)
                .orElse(null);
    }

    // UPDATE INVOICE
    @Override
    public Invoice updateInvoice(
            Long id,
            Invoice invoice) {

        Invoice existingInvoice =
                invoiceRepository
                        .findById(id)
                        .orElse(null);

        if (existingInvoice == null) {
            return null;
        }

        existingInvoice.setPaymentStatus(
                invoice.getPaymentStatus());

        return invoiceRepository.save(
                existingInvoice);
    }

    // DELETE INVOICE
    @Override
    public String deleteInvoice(Long id) {

        if (invoiceRepository.existsById(id)) {

            invoiceRepository.deleteById(id);

            return "Invoice with ID "
                    + id
                    + " deleted successfully.";
        }

        return "Invoice with ID "
                + id
                + " not found.";
    }
}