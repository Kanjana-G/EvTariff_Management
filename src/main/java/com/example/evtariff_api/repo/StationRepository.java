package com.example.evtariff_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.evtariff_api.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

}