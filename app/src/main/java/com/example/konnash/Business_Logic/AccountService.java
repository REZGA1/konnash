package com.example.konnash.Business_Logic;

import android.content.Context;

import com.example.konnash.Database.AppSettingsDAO;
import com.example.konnash.Database.BusinessCardDAO;
import com.example.konnash.Database.CategoryDAO;
import com.example.konnash.Database.TransactionArchiveDAO;
import com.example.konnash.Database.TransactionDAO;
import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Model.AppSettings;

public class AccountService {

    private final UserProfileDAO        userProfileDAO;
    private final BusinessCardDAO       businessCardDAO;
    private final AppSettingsDAO        appSettingsDAO;
    private final TransactionDAO        transactionDAO;
    private final TransactionArchiveDAO archiveDAO;
    private final CategoryDAO           categoryDAO;

    public AccountService(Context context) {
        userProfileDAO  = new UserProfileDAO(context);
        businessCardDAO = new BusinessCardDAO(context);
        appSettingsDAO  = new AppSettingsDAO(context);
        transactionDAO  = new TransactionDAO(context);
        archiveDAO      = new TransactionArchiveDAO(context);
        categoryDAO     = new CategoryDAO(context);
    }

    public void deleteAccount() {

        // 1. احفظ اللغة قبل الحذف
        AppSettings current  = appSettingsDAO.get();
        String      language = (current != null) ? current.getLanguage() : "ar";

        // 2. احذف كل شيء
        transactionDAO.deleteAll();
        archiveDAO.deleteAll();
        categoryDAO.deleteAll();
        userProfileDAO.delete();
        businessCardDAO.delete();
        appSettingsDAO.delete();

        // 3. أعد حفظ اللغة فقط بدون رمز سري
        appSettingsDAO.insert(new AppSettings(language, null));
    }
}