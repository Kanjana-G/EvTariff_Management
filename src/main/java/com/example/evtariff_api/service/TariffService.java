package com.example.evtariff_api.service;

import java.time.LocalTime;
import java.util.List;

import com.example.evtariff_api.model.Tariff;

public interface TariffService {

    Tariff addTariff(Tariff tariff);

    List<Tariff> getAllTariffs();

    Tariff getTariffById(Long id);

    Tariff updateTariff(Long id, Tariff tariff);

    void deleteTariff(Long id);

    double calculateRate(Long id, LocalTime time);
}