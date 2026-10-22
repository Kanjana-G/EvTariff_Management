package com.example.evtariff_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.model.UtilityExpense;
import com.example.evtariff_api.repo.StationRepository;
import com.example.evtariff_api.repo.UtilityExpenseRepository;

@Service
public class UtilityExpenseServiceImpl
        implements UtilityExpenseService {

    private final UtilityExpenseRepository expenseRepository;
    private final StationRepository stationRepository;

    public UtilityExpenseServiceImpl(
            UtilityExpenseRepository expenseRepository,
            StationRepository stationRepository) {

        this.expenseRepository = expenseRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public UtilityExpense addExpense(UtilityExpense expense) {

        if (expense.getStation() == null) {
            return null;
        }

        Long stationId = expense.getStation().getId();

        Station station =
                stationRepository.findById(stationId).orElse(null);

        if (station == null) {
            return null;
        }

        expense.setStation(station);

        return expenseRepository.save(expense);
    }

    @Override
    public List<UtilityExpense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public UtilityExpense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
public UtilityExpense updateExpense(
        Long id, UtilityExpense expense) {

    UtilityExpense existingExpense =
            expenseRepository.findById(id).orElse(null);

    if (existingExpense == null) {
        return null;
    }

    existingExpense.setExpenseType(
            expense.getExpenseType());

    existingExpense.setProviderName(
            expense.getProviderName());

    existingExpense.setAmount(
            expense.getAmount());

    existingExpense.setExpenseDate(
            expense.getExpenseDate());

    existingExpense.setPaymentMethod(
            expense.getPaymentMethod());

    existingExpense.setReferenceNumber(
            expense.getReferenceNumber());

    existingExpense.setPaymentStatus(
            expense.getPaymentStatus());

    if (expense.getStation() != null) {

        Station station = stationRepository
                .findById(expense.getStation().getId())
                .orElse(null);

        if (station != null) {
            existingExpense.setStation(station);
        }
    }

    return expenseRepository.save(existingExpense);
}
    @Override
    public String deleteExpense(Long id) {

        if (expenseRepository.existsById(id)) {

            expenseRepository.deleteById(id);

            return "Utility Expense with ID "
                    + id
                    + " deleted successfully.";
        }

        return "Utility Expense with ID "
                + id
                + " not found.";
    }
}