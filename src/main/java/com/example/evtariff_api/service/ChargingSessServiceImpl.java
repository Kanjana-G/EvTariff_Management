package com.example.evtariff_api.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.evtariff_api.model.Charger;
import com.example.evtariff_api.model.ChargingSession;
import com.example.evtariff_api.model.Tariff;
import com.example.evtariff_api.repo.ChargerRepository;
import com.example.evtariff_api.repo.ChargingSessRepository;
import com.example.evtariff_api.repo.TariffRepository;

@Service
public class ChargingSessServiceImpl
        implements ChargingSessService {

    private final ChargingSessRepository sessionRepository;
    private final ChargerRepository chargerRepository;
    private final TariffRepository tariffRepository;

    public ChargingSessServiceImpl(
            ChargingSessRepository sessionRepository,
            ChargerRepository chargerRepository,
            TariffRepository tariffRepository) {

        this.sessionRepository = sessionRepository;
        this.chargerRepository = chargerRepository;
        this.tariffRepository = tariffRepository;
    }

    // START CHARGING SESSION
    @Override
    public ChargingSession startSession(
            ChargingSession session) {

        // Check whether charger and tariff are provided
        if (session.getCharger() == null
                || session.getTariff() == null) {
            return null;
        }

        // Get charger from database
        Charger charger = chargerRepository
                .findById(session.getCharger().getId())
                .orElse(null);

        // Get tariff from database
        Tariff tariff = tariffRepository
                .findById(session.getTariff().getId())
                .orElse(null);

        // Charger or tariff not found
        if (charger == null || tariff == null) {
            return null;
        }

        // Charging can start only if charger is AVAILABLE
        if (!"AVAILABLE".equalsIgnoreCase(
                charger.getStatus())) {
            return null;
        }

        session.setCharger(charger);
        session.setTariff(tariff);

        session.setStartTime(LocalDateTime.now());
        session.setEndTime(null);

        session.setEnergyConsumed(0.0);
        session.setRateApplied(0.0);
        session.setTotalAmount(0.0);

        session.setStatus("ACTIVE");

        // Charger becomes occupied
        charger.setStatus("OCCUPIED");
        chargerRepository.save(charger);

        return sessionRepository.save(session);
    }

    // GET ALL SESSIONS
    @Override
    public List<ChargingSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    // GET SESSION BY ID
    @Override
    public ChargingSession getSessionById(Long id) {
        return sessionRepository.findById(id).orElse(null);
    }

    // STOP CHARGING SESSION
    @Override
    public ChargingSession stopSession(
            Long id,
            double energyConsumed) {

        ChargingSession session =
                sessionRepository.findById(id).orElse(null);

        if (session == null) {
            return null;
        }

        // Prevent stopping the same session again
        if ("COMPLETED".equalsIgnoreCase(
                session.getStatus())) {
            return null;
        }

        session.setEndTime(LocalDateTime.now());
        session.setEnergyConsumed(energyConsumed);

        Tariff tariff = session.getTariff();

        LocalTime currentTime = LocalTime.now();

        // Normal/base tariff
        double rate = tariff.getBaseRate();

        // Check whether current time is peak time
        if (!currentTime.isBefore(
                tariff.getPeakStartTime())
                && currentTime.isBefore(
                        tariff.getPeakEndTime())) {

            rate = tariff.getBaseRate()
                    * tariff.getPeakMultiplier();
        }

        session.setRateApplied(rate);

        double totalAmount =
                energyConsumed * rate;

        session.setTotalAmount(totalAmount);

        session.setStatus("COMPLETED");

        // Charger becomes available again
        Charger charger = session.getCharger();

        charger.setStatus("AVAILABLE");

        chargerRepository.save(charger);

        return sessionRepository.save(session);
    }

    // UPDATE SESSION
    @Override
    public ChargingSession updateSession(
            Long id,
            ChargingSession session) {

        ChargingSession existingSession =
                sessionRepository.findById(id).orElse(null);

        if (existingSession == null) {
            return null;
        }

        existingSession.setDriverName(
                session.getDriverName());

        existingSession.setVehicleNumber(
                session.getVehicleNumber());

        return sessionRepository.save(existingSession);
    }

    // DELETE SESSION
    @Override
    public String deleteSession(Long id) {

        if (sessionRepository.existsById(id)) {

            sessionRepository.deleteById(id);

            return "Charging Session with ID "
                    + id
                    + " deleted successfully.";
        }

        return "Charging Session with ID "
                + id
                + " not found.";
    }
}