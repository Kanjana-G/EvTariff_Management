package com.example.evtariff_api.service;

import com.example.evtariff_api.model.Budget;
import com.example.evtariff_api.model.ChargingSession;
import com.example.evtariff_api.model.Payment;
import com.example.evtariff_api.model.Station;
import com.example.evtariff_api.model.UtilityExpense;

import com.example.evtariff_api.repo.BudgetRepository;
import com.example.evtariff_api.repo.ChargingSessRepository;
import com.example.evtariff_api.repo.PaymentRepository;
import com.example.evtariff_api.repo.StationRepository;
import com.example.evtariff_api.repo.UtilityExpenseRepository;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final PaymentRepository paymentRepository;
    private final UtilityExpenseRepository utilityExpenseRepository;
    private final StationRepository stationRepository;
    private final ChargingSessRepository chargingSessionRepository;
    private final BudgetRepository budgetRepository;


    public ReportServiceImpl(
            PaymentRepository paymentRepository,
            UtilityExpenseRepository utilityExpenseRepository,
            StationRepository stationRepository,
            ChargingSessRepository chargingSessionRepository,
            BudgetRepository budgetRepository) {

        this.paymentRepository = paymentRepository;
        this.utilityExpenseRepository = utilityExpenseRepository;
        this.stationRepository = stationRepository;
        this.chargingSessionRepository = chargingSessionRepository;
        this.budgetRepository = budgetRepository;
    }


    /* =========================================
       PROFIT AND LOSS
    ========================================= */

    @Override
    public Map<String, Object> getProfitAndLoss() {

        List<Payment> payments =
                paymentRepository.findAll();

        List<UtilityExpense> expenses =
                utilityExpenseRepository.findAll();


        double totalRevenue = 0.0;

        double totalExpenses = 0.0;


        for (Payment payment : payments) {

            if (payment.getPaymentStatus() != null
                    &&
                payment.getPaymentStatus()
                        .equalsIgnoreCase("SUCCESS")) {

                if (payment.getAmount() != null) {

                    totalRevenue +=
                            payment.getAmount();
                }
            }
        }


        for (UtilityExpense expense : expenses) {

            if (expense.getPaymentStatus() != null
                    &&
                expense.getPaymentStatus()
                        .equalsIgnoreCase("PAID")) {

                if (expense.getAmount() != null) {

                    totalExpenses +=
                            expense.getAmount();
                }
            }
        }


        double profitOrLoss =
                totalRevenue - totalExpenses;


        String result;

        if (profitOrLoss > 0) {

            result = "PROFIT";

        } else if (profitOrLoss < 0) {

            result = "LOSS";

        } else {

            result = "BREAK EVEN";
        }


        Map<String, Object> report =
                new LinkedHashMap<>();


        report.put(
                "totalRevenue",
                totalRevenue
        );

        report.put(
                "totalExpenses",
                totalExpenses
        );

        report.put(
                "profitOrLoss",
                profitOrLoss
        );

        report.put(
                "result",
                result
        );


        return report;
    }


    /* =========================================
       SITE YIELD
    ========================================= */

    @Override
    public Map<String, Object> getSiteYield(
            Long stationId) {


        Station station =
                stationRepository
                        .findById(stationId)
                        .orElse(null);


        if (station == null) {
            return null;
        }


        List<ChargingSession> sessions =
                chargingSessionRepository.findAll();


        List<UtilityExpense> expenses =
                utilityExpenseRepository.findAll();


        int completedSessions = 0;

        double totalEnergy = 0.0;

        double chargingRevenue = 0.0;

        double utilityExpenses = 0.0;


        /* -------------------------
           CHARGING DATA
        ------------------------- */

        for (ChargingSession session : sessions) {

            if (session.getCharger() == null
                    ||
                session.getCharger()
                        .getStation() == null) {

                continue;
            }


            Long sessionStationId =
                    session
                            .getCharger()
                            .getStation()
                            .getId();


            if (
                sessionStationId.equals(stationId)
                &&
                session.getStatus() != null
                &&
                session.getStatus()
                        .equalsIgnoreCase(
                                "COMPLETED"
                        )
            ) {

                completedSessions++;


                if (
                    session.getEnergyConsumed()
                    != null
                ) {

                    totalEnergy +=
                            session
                                    .getEnergyConsumed();
                }


                if (
                    session.getTotalAmount()
                    != null
                ) {

                    chargingRevenue +=
                            session
                                    .getTotalAmount();
                }
            }
        }


        /* -------------------------
           EXPENSE DATA
        ------------------------- */

        for (UtilityExpense expense : expenses) {

            if (expense.getStation() == null) {
                continue;
            }


            if (
                expense
                        .getStation()
                        .getId()
                        .equals(stationId)
                &&
                expense.getPaymentStatus() != null
                &&
                expense
                        .getPaymentStatus()
                        .equalsIgnoreCase("PAID")
            ) {

                if (expense.getAmount() != null) {

                    utilityExpenses +=
                            expense.getAmount();
                }
            }
        }


        double netYield =
                chargingRevenue
                -
                utilityExpenses;


        Map<String, Object> report =
                new LinkedHashMap<>();


        report.put(
                "stationId",
                station.getId()
        );

        report.put(
                "stationName",
                station.getStationName()
        );

        report.put(
                "location",
                station.getLocation()
        );

        report.put(
                "completedSessions",
                completedSessions
        );

        report.put(
                "totalEnergyDelivered",
                totalEnergy
        );

        report.put(
                "chargingRevenue",
                chargingRevenue
        );

        report.put(
                "utilityExpenses",
                utilityExpenses
        );

        report.put(
                "netYield",
                netYield
        );


        return report;
    }


    /* =========================================
       BUDGET REPORT
    ========================================= */

    @Override
    public Map<String, Object> getBudgetReport(
            Long budgetId) {


        Budget budget =
                budgetRepository
                        .findById(budgetId)
                        .orElse(null);


        if (budget == null) {
            return null;
        }


        double budgetAmount =
                budget.getBudgetAmount() == null
                        ? 0.0
                        : budget.getBudgetAmount();


        double spentAmount =
                budget.getSpentAmount() == null
                        ? 0.0
                        : budget.getSpentAmount();


        double remainingAmount =
                budgetAmount
                -
                spentAmount;


        double usedPercentage = 0.0;


        if (budgetAmount > 0) {

            usedPercentage =
                    (spentAmount
                    / budgetAmount)
                    * 100;
        }


        Map<String, Object> report =
                new LinkedHashMap<>();


        report.put(
                "budgetId",
                budget.getId()
        );

        report.put(
                "budgetName",
                budget.getBudgetName()
        );


        if (budget.getStation() != null) {

            report.put(
                    "stationName",
                    budget
                            .getStation()
                            .getStationName()
            );

        } else {

            report.put(
                    "stationName",
                    "-"
            );
        }


        report.put(
                "budgetAmount",
                budgetAmount
        );

        report.put(
                "spentAmount",
                spentAmount
        );

        report.put(
                "remainingAmount",
                remainingAmount
        );

        report.put(
                "usedPercentage",
                usedPercentage
        );

        report.put(
                "startDate",
                budget.getStartDate()
        );

        report.put(
                "endDate",
                budget.getEndDate()
        );


        return report;
    }
}