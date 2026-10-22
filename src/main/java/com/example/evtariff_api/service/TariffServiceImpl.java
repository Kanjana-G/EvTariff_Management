package com.example.evtariff_api.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.model.Tariff;
import com.example.evtariff_api.repo.StationRepository;
import com.example.evtariff_api.repo.TariffRepository;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final StationRepository stationRepository;

    public TariffServiceImpl(TariffRepository tariffRepository,
                             StationRepository stationRepository) {
        this.tariffRepository = tariffRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Tariff addTariff(Tariff tariff) {

        if (tariff.getStation() != null) {

            Long stationId = tariff.getStation().getId();

            Station station = stationRepository
                    .findById(stationId)
                    .orElse(null);

            tariff.setStation(station);
        }

        return tariffRepository.save(tariff);
    }

    @Override
    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    @Override
    public Tariff getTariffById(Long id) {
        return tariffRepository.findById(id).orElse(null);
    }

    @Override
    public Tariff updateTariff(Long id, Tariff tariff) {

        Tariff existingTariff =
                tariffRepository.findById(id).orElse(null);

        if (existingTariff != null) {

            existingTariff.setBaseRate(tariff.getBaseRate());
            existingTariff.setPeakStartTime(tariff.getPeakStartTime());
            existingTariff.setPeakEndTime(tariff.getPeakEndTime());
            existingTariff.setPeakMultiplier(tariff.getPeakMultiplier());

            if (tariff.getStation() != null) {

                Long stationId = tariff.getStation().getId();

                Station station = stationRepository
                        .findById(stationId)
                        .orElse(null);

                existingTariff.setStation(station);
            }

            return tariffRepository.save(existingTariff);
        }

        return null;
    }

    @Override
    public void deleteTariff(Long id) {
        tariffRepository.deleteById(id);
    }

    @Override
    public double calculateRate(Long id, LocalTime time) {

        Tariff tariff = tariffRepository.findById(id).orElse(null);

        if (tariff == null) {
            return 0;
        }

        if (!time.isBefore(tariff.getPeakStartTime())
                && time.isBefore(tariff.getPeakEndTime())) {

            return tariff.getBaseRate()
                    * tariff.getPeakMultiplier();
        }

        return tariff.getBaseRate();
    }
}