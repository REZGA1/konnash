package com.example.konnash.Business_Logic;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageManager {

    public static final String ARABIC  = "ar";
    public static final String ENGLISH = "en";
    public static final String FRENCH  = "fr";
    public static final String TURKISH = "tr";

    // تطبيق اللغة — استدعِها في attachBaseContext في كل Activity
    public static Context applyLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    // التحقق من أن الكود مدعوم
    public static boolean isSupported(String code) {
        return ARABIC.equals(code)  ||
                ENGLISH.equals(code) ||
                FRENCH.equals(code)  ||
                TURKISH.equals(code);
    }
}