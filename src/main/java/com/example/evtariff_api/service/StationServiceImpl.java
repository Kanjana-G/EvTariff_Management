package com.example.evtariff_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.repo.StationRepository;

@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public Station addStation(Station station) {
        return stationRepository.save(station);
    }

    @Override
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @Override
    public Station getStationById(Long id) {
        return stationRepository.findById(id).orElse(null);
    }

    @Override
    public Station updateStation(Long id, Station station) {

        Station existingStation = stationRepository.findById(id).orElse(null);

        if (existingStation != null) {

            existingStation.setStationName(station.getStationName());
            existingStation.setLocation(station.getLocation());
            existingStation.setStatus(station.getStatus());

            return stationRepository.save(existingStation);
        }

        return null;
    }

    @Override
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }
}