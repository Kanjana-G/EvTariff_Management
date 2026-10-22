
package com.example.evtariff_api.service;

import java.util.List;
import com.example.evtariff_api.model.Station;

public interface StationService {

    Station addStation(Station station);

    List<Station> getAllStations();

    Station getStationById(Long id);

    Station updateStation(Long id, Station station);

    void deleteStation(Long id);
}

