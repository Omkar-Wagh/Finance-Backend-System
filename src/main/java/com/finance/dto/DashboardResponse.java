package com.finance.dto;

import java.util.Map;

public class DashboardResponse {
    private double totalIncome;
    private double totalExpense;
    private double netBalance;
    private Map<String, CategoryStats> categoryWise;

    public DashboardResponse(double totalIncome, double totalExpense, double netBalance, Map<String, CategoryStats> categoryWise) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
        this.categoryWise = categoryWise;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(double netBalance) {
        this.netBalance = netBalance;
    }

    public Map<String, CategoryStats> getCategoryWise() {
        return categoryWise;
    }

    public void setCategoryWise(Map<String, CategoryStats> categoryWise) {
        this.categoryWise = categoryWise;
    }
}