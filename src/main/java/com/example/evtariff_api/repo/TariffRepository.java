package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.evtariff_api.model.Tariff;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

}