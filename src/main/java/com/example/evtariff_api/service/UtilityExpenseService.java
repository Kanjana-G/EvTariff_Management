package com.example.evtariff_api.service;

import java.util.List;

import com.example.evtariff_api.model.UtilityExpense;

public interface UtilityExpenseService {

    UtilityExpense addExpense(UtilityExpense expense);

    List<UtilityExpense> getAllExpenses();

    UtilityExpense getExpenseById(Long id);

    UtilityExpense updateExpense(Long id, UtilityExpense expense);

    String deleteExpense(Long id);
}