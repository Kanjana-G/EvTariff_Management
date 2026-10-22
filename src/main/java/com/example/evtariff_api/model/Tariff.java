package com.example.evtariff_api.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double baseRate;
    private LocalTime peakStartTime;
    private LocalTime peakEndTime;
    private Double peakMultiplier;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public Tariff() {
    }

    public Tariff(Double baseRate, LocalTime peakStartTime,
                  LocalTime peakEndTime, Double peakMultiplier,
                  Station station) {

        this.baseRate = baseRate;
        this.peakStartTime = peakStartTime;
        this.peakEndTime = peakEndTime;
        this.peakMultiplier = peakMultiplier;
        this.station = station;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(Double baseRate) {
        this.baseRate = baseRate;
    }

    public LocalTime getPeakStartTime() {
        return peakStartTime;
    }

    public void setPeakStartTime(LocalTime peakStartTime) {
        this.peakStartTime = peakStartTime;
    }

    public LocalTime getPeakEndTime() {
        return peakEndTime;
    }

    public void setPeakEndTime(LocalTime peakEndTime) {
        this.peakEndTime = peakEndTime;
    }

    public Double getPeakMultiplier() {
        return peakMultiplier;
    }

    public void setPeakMultiplier(Double peakMultiplier) {
        this.peakMultiplier = peakMultiplier;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}