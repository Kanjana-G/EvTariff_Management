package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.Charger;

public interface ChargerRepository extends JpaRepository<Charger, Long> {

}