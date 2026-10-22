package com.example.evtariff_api.service;

import java.util.List;

import com.example.evtariff_api.model.ChargingSession;

public interface ChargingSessService {

    ChargingSession startSession(ChargingSession session);

    List<ChargingSession> getAllSessions();

    ChargingSession getSessionById(Long id);

    ChargingSession stopSession(Long id, double energyConsumed);

    ChargingSession updateSession(Long id, ChargingSession session);

    String deleteSession(Long id);
}