package com.example.evtariff_api.service;

import java.util.List;

import com.example.evtariff_api.model.Invoice;

public interface InvoiceService {

    Invoice createInvoice(Long sessionId);

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long id);

    Invoice updateInvoice(Long id, Invoice invoice);

    String deleteInvoice(Long id);
}