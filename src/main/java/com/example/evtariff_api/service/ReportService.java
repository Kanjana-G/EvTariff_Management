package com.example.evtariff_api.service;

import java.util.Map;

public interface ReportService {

    Map<String, Object> getProfitAndLoss();

    Map<String, Object> getSiteYield(Long stationId);

    Map<String, Object> getBudgetReport(Long budgetId);
}