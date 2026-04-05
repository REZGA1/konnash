package com.example.konnash.Model;

public class UserProfile {

    private String storeName;
    private String phone;
    private String countryCode;
    private String activityType;
    private String sector;

    private double income;   // الدخل
    private double expense;  // المصروف
    private long openDate; // الوقت

    public UserProfile(String storeName, String phone, String countryCode,
                       String activityType, String sector,
                       double income,double expense, long openDate) {

        this.storeName = storeName;
        this.phone = phone;
        this.countryCode = countryCode;
        this.activityType = activityType;
        this.sector = sector;
        this.income  = income;
        this.expense = expense;
        this.openDate = openDate;
    }

    // Getters
    public String getStoreName() { return storeName; }
    public String getPhone() { return phone; }
    public String getCountryCode() { return countryCode; }
    public String getActivityType() { return activityType; }
    public String getSector() { return sector; }
    public double getBalance() { return income - expense; }
    public double getIncome()  { return income;  }
    public double getExpense() { return expense; }
    public long getOpenDate() { return openDate; }

    // Setters
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public void setSector(String sector) { this.sector = sector; }
    public void setIncome(double income)   { this.income  = income;  }
    public void setExpense(double expense) { this.expense = expense; }
    public void setOpenDate(long openDate) { this.openDate = openDate; }
}