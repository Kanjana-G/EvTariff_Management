package com.example.evtariff_api.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String budgetName;

    private Double budgetAmount;

    private Double spentAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public Budget() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(Double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}