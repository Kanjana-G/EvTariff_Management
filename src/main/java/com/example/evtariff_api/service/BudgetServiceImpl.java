package com.example.evtariff_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Budget;
import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.repo.BudgetRepository;
import com.example.evtariff_api.repo.StationRepository;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final StationRepository stationRepository;

    public BudgetServiceImpl(
            BudgetRepository budgetRepository,
            StationRepository stationRepository) {

        this.budgetRepository = budgetRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Budget addBudget(Budget budget) {

        if (budget.getStation() == null) {
            return null;
        }

        Long stationId = budget.getStation().getId();

        Station station =
                stationRepository.findById(stationId).orElse(null);

        if (station == null) {
            return null;
        }

        budget.setStation(station);

        if (budget.getSpentAmount() == null) {
            budget.setSpentAmount(0.0);
        }

        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id).orElse(null);
    }

    @Override
    public Budget updateBudget(Long id, Budget budget) {

        Budget existingBudget =
                budgetRepository.findById(id).orElse(null);

        if (existingBudget == null) {
            return null;
        }

        existingBudget.setBudgetName(
                budget.getBudgetName());

        existingBudget.setBudgetAmount(
                budget.getBudgetAmount());

        existingBudget.setSpentAmount(
                budget.getSpentAmount());

        existingBudget.setStartDate(
                budget.getStartDate());

        existingBudget.setEndDate(
                budget.getEndDate());

        if (budget.getStation() != null) {

            Station station = stationRepository
                    .findById(budget.getStation().getId())
                    .orElse(null);

            if (station != null) {
                existingBudget.setStation(station);
            }
        }

        return budgetRepository.save(existingBudget);
    }

    @Override
    public Double getRemainingBudget(Long id) {

        Budget budget =
                budgetRepository.findById(id).orElse(null);

        if (budget == null) {
            return null;
        }

        return budget.getBudgetAmount()
                - budget.getSpentAmount();
    }

    @Override
    public String deleteBudget(Long id) {

        if (budgetRepository.existsById(id)) {

            budgetRepository.deleteById(id);

            return "Budget with ID "
                    + id
                    + " deleted successfully.";
        }

        return "Budget with ID "
                + id
                + " not found.";
    }
}