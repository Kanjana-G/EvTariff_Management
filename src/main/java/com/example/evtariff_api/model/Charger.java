package com.example.evtariff_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Charger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chargerName;
    private String chargerType;
    private String status;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public Charger() {
    }

    public Charger(String chargerName, String chargerType,
                   String status, Station station) {
        this.chargerName = chargerName;
        this.chargerType = chargerType;
        this.status = status;
        this.station = station;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargerName() {
        return chargerName;
    }

    public void setChargerName(String chargerName) {
        this.chargerName = chargerName;
    }

    public String getChargerType() {
        return chargerType;
    }

    public void setChargerType(String chargerType) {
        this.chargerType = chargerType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}