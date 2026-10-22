package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.Budget;

public interface BudgetRepository
        extends JpaRepository<Budget, Long> {

}