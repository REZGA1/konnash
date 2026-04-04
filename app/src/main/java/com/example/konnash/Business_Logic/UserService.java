package com.example.konnash.Business_Logic;

import com.example.konnash.Model.UserProfile;

public class UserService {

    // Validation
    public boolean isValid(UserProfile profile) {

        if (profile.getStoreName() == null || profile.getStoreName().isEmpty())
            return false;

        if (profile.getPhone() == null || profile.getPhone().isEmpty())
            return false;

        if (profile.getCountryCode() == null || profile.getCountryCode().isEmpty())
            return false;

        return true;
    }

    // إضافة دخل
    public double addIncome(double current, double amount) {
        return current + Math.abs(amount);
    }

    // إضافة مصروف
    public double addExpense(double current, double amount) {
        return current + Math.abs(amount);
    }
}