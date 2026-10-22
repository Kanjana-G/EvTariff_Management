package com.example.evtariff_api.service;

import java.util.List;

import com.example.evtariff_api.model.Payment;

public interface PaymentService {

    Payment makePayment(Long invoiceId, String paymentMethod);

    List<Payment> getAllPayments();

    Payment getPaymentById(Long id);

    Payment updatePayment(Long id, Payment payment);

    String deletePayment(Long id);
}