package com.example.models;
import java.time.*;

import java.time.LocalDate;

public class Subscription {
    private int subscriptionID;
    private String subscriptionsName;
    private double cost;
    private boolean isRecurring;
    private String billingCycleType;
    private LocalDate billingCycleDate;
    private int userID; // Foreign key reference to User

    public Subscription() {}

    public Subscription(int subscriptionID, String subscriptionsName, double cost, boolean isRecurring,
                        String billingCycleType, LocalDate billingCycleDate, int userID) {
        this.subscriptionID = subscriptionID;
        this.subscriptionsName = subscriptionsName;
        this.cost = cost;
        this.isRecurring = isRecurring;
        this.billingCycleType = billingCycleType;
        this.billingCycleDate = billingCycleDate;
        this.userID = userID;
    }

    // Getters
    public int getSubscriptionID() {
        return subscriptionID;
    }

    public String getSubscriptionsName() {
        return subscriptionsName;
    }

    public double getCost() {
        return cost;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public String getBillingCycleType() {
        return billingCycleType;
    }

    public LocalDate getBillingCycleDate() {
        return billingCycleDate;
    }

    public int getUserID() {
        return userID;
    }

    // Setters
    public void setSubscriptionID(int subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public void setSubscriptionsName(String subscriptionsName) {
        this.subscriptionsName = subscriptionsName;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public void setBillingCycleType(String billingCycleType) {
        this.billingCycleType = billingCycleType;
    }

    public void setBillingCycleDate(LocalDate billingCycleDate) {
        this.billingCycleDate = billingCycleDate;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionID=" + subscriptionID +
                ", subscriptionsName='" + subscriptionsName + '\'' +
                ", cost=" + cost +
                ", isRecurring=" + isRecurring +
                ", billingCycleType='" + billingCycleType + '\'' +
                ", billingCycleDate=" + billingCycleDate +
                ", userID=" + userID +
                '}';
    }
}
