package com.example.evtariff_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Invoice;
import com.example.evtariff_api.model.Payment;
import com.example.evtariff_api.repo.InvoiceRepository;
import com.example.evtariff_api.repo.PaymentRepository;

@Service
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            InvoiceRepository invoiceRepository) {

        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // MAKE PAYMENT
    @Override
    public Payment makePayment(
            Long invoiceId,
            String paymentMethod) {

        Invoice invoice =
                invoiceRepository
                        .findById(invoiceId)
                        .orElse(null);

        // Invoice must exist
        if (invoice == null) {
            return null;
        }

        // Prevent paying same invoice again
        if ("PAID".equalsIgnoreCase(
                invoice.getPaymentStatus())) {

            return null;
        }

        Payment payment = new Payment();

        payment.setInvoice(invoice);

        // Payment amount automatically comes
        // from the invoice
        payment.setAmount(
                invoice.getAmount());

        payment.setPaymentMethod(
                paymentMethod);

        payment.setPaymentStatus(
                "SUCCESS");

        payment.setPaymentDate(
                LocalDateTime.now());

        // Change invoice status automatically
        invoice.setPaymentStatus("PAID");

        invoiceRepository.save(invoice);

        return paymentRepository.save(payment);
    }

    // GET ALL PAYMENTS
    @Override
    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    // GET PAYMENT BY ID
    @Override
    public Payment getPaymentById(Long id) {

        return paymentRepository
                .findById(id)
                .orElse(null);
    }

    // UPDATE PAYMENT
    @Override
    public Payment updatePayment(
            Long id,
            Payment payment) {

        Payment existingPayment =
                paymentRepository
                        .findById(id)
                        .orElse(null);

        if (existingPayment == null) {
            return null;
        }

        existingPayment.setPaymentMethod(
                payment.getPaymentMethod());

        existingPayment.setPaymentStatus(
                payment.getPaymentStatus());

        return paymentRepository.save(
                existingPayment);
    }

    // DELETE PAYMENT
    @Override
    public String deletePayment(Long id) {

        if (paymentRepository.existsById(id)) {

            paymentRepository.deleteById(id);

            return "Payment with ID "
                    + id
                    + " deleted successfully.";
        }

        return "Payment with ID "
                + id
                + " not found.";
    }
}