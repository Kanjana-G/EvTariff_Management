package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}