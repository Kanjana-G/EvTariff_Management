package com.example.evtariff_api.service;

import java.util.List;

import com.example.evtariff_api.model.Charger;

public interface ChargerService {

    Charger addCharger(Charger charger);

    List<Charger> getAllChargers();

    Charger getChargerById(Long id);

    Charger updateCharger(Long id, Charger charger);

    void deleteCharger(Long id);
}