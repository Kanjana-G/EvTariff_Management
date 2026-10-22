package com.example.evtariff_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Charger;
import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.repo.ChargerRepository;
import com.example.evtariff_api.repo.StationRepository;

@Service
public class ChargerServiceImpl implements ChargerService {

    private final ChargerRepository chargerRepository;
    private final StationRepository stationRepository;

    public ChargerServiceImpl(ChargerRepository chargerRepository,
                              StationRepository stationRepository) {
        this.chargerRepository = chargerRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Charger addCharger(Charger charger) {

        if (charger.getStation() != null) {

            Long stationId = charger.getStation().getId();

            Station station = stationRepository
                    .findById(stationId)
                    .orElse(null);

            charger.setStation(station);
        }

        return chargerRepository.save(charger);
    }

    @Override
    public List<Charger> getAllChargers() {
        return chargerRepository.findAll();
    }

    @Override
    public Charger getChargerById(Long id) {
        return chargerRepository.findById(id).orElse(null);
    }

    @Override
    public Charger updateCharger(Long id, Charger charger) {

        Charger existingCharger =
                chargerRepository.findById(id).orElse(null);

        if (existingCharger != null) {

            existingCharger.setChargerName(charger.getChargerName());
            existingCharger.setChargerType(charger.getChargerType());
            existingCharger.setStatus(charger.getStatus());

            if (charger.getStation() != null) {

                Long stationId = charger.getStation().getId();

                Station station = stationRepository
                        .findById(stationId)
                        .orElse(null);

                existingCharger.setStation(station);
            }

            return chargerRepository.save(existingCharger);
        }

        return null;
    }

    @Override
    public void deleteCharger(Long id) {
        chargerRepository.deleteById(id);
    }
}