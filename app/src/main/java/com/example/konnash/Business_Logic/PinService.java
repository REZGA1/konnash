package com.example.konnash.Business_Logic;

public class PinService {
    // التحقق أن الرمز 4 أرقام بالضبط
    public boolean isValidPin(String pin) {
        if (pin == null) return false;
        return pin.matches("\\d{4}");
    }

    // مقارنة الرمز المُدخل بالمحفوظ
    public boolean checkPin(String entered, String stored) {
        if (stored == null || stored.isEmpty()) return true; // لا يوجد رمز = حر
        return entered != null && entered.equals(stored);
    }

    // تفعيل الرمز — يرجع false إذا الرمز غير صالح
    public String enablePin(String pin) {
        if (!isValidPin(pin)) return null;
        return pin;
    }

    // تعطيل الرمز
    public String disablePin() {
        return null;
    }
}
