package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.Invoice;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    boolean existsByChargingSessionId(Long sessionId);
}