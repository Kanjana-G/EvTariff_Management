package com.example.evtariff_api.service;

import java.util.List;

import com.example.evtariff_api.model.Budget;

public interface BudgetService {

    Budget addBudget(Budget budget);

    List<Budget> getAllBudgets();

    Budget getBudgetById(Long id);

    Budget updateBudget(Long id, Budget budget);

    Double getRemainingBudget(Long id);

    String deleteBudget(Long id);
}