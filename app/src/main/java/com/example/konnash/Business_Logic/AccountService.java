package com.example.konnash.Business_Logic;


import android.content.Context;

import com.example.konnash.Database.AppSettingsDAO;
import com.example.konnash.Database.BusinessCardDAO;
import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Model.AppSettings;

public class AccountService {

    private final UserProfileDAO  userProfileDAO;
    private final BusinessCardDAO businessCardDAO;
    private final AppSettingsDAO  appSettingsDAO;

    public AccountService(Context context) {
        userProfileDAO  = new UserProfileDAO(context);
        businessCardDAO = new BusinessCardDAO(context);
        appSettingsDAO  = new AppSettingsDAO(context);
    }

    public void deleteAccount() {

        // 1. احفظ اللغة قبل الحذف
        AppSettings current  = appSettingsDAO.get();
        String      language = (current != null) ? current.getLanguage() : "ar";

        // 2. احذف كل شيء
        userProfileDAO.delete();
        businessCardDAO.delete();
        appSettingsDAO.delete();

        // 3. أعد حفظ اللغة فقط بدون رمز سري
        appSettingsDAO.insert(new AppSettings(language, null));
    }
}