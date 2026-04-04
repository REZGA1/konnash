package com.example.konnash.Model;

public class AppSettings {

    private String language; // "ar" | "en" | "fr" | "tr"
    private String pinCode;  // null أو "" = معطّل | "1234" = مفعّل

    public AppSettings(String language, String pinCode) {
        this.language = language;
        this.pinCode  = pinCode;
    }

    // Getters
    public String getLanguage() { return language; }
    public String getPinCode()  { return pinCode;  }

    // Setters
    public void setLanguage(String language) { this.language = language; }
    public void setPinCode(String pinCode)   { this.pinCode  = pinCode;  }

    // Helper
    public boolean isPinEnabled() {
        return pinCode != null && pinCode.length() == 4;
    }
}