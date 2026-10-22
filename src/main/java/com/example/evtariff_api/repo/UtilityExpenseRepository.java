package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.UtilityExpense;

public interface UtilityExpenseRepository
        extends JpaRepository<UtilityExpense, Long> {

}