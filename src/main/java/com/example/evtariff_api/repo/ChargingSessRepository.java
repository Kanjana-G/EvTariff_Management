package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.ChargingSession;

public interface ChargingSessRepository
        extends JpaRepository<ChargingSession, Long> {

}