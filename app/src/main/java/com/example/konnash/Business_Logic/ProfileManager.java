package com.example.konnash.Business_Logic;

import android.content.Context;

import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Model.UserProfile;

public class ProfileManager {

    private final UserProfileDAO dao;
    private static final int PROFILE_ID = 1;

    public ProfileManager(Context context) {
        dao = new UserProfileDAO(context);
    }

    // ─── إنشاء الملف الشخصي ────────────────────────────
    public boolean createProfile(String storeName, String phone, String countryCode,
                                 String activityType, String sector,
                                 double initialBalance, String openDate) {
        if (dao.exists()) return false;

        UserProfile profile = new UserProfile(
                PROFILE_ID, storeName, phone, countryCode,
                activityType, sector, initialBalance, openDate
        );
        return dao.insert(profile);
    }

    // ─── جلب بيانات المستخدم ───────────────────────────
    public UserProfile getProfile() {
        return dao.get();
    }

    // ─── تعديل بيانات المتجر ───────────────────────────
    public boolean updateProfile(String storeName, String phone, String countryCode,
                                 String activityType, String sector) {
        UserProfile profile = dao.get();
        if (profile == null) return false;

        profile.setStoreName(storeName);
        profile.setPhone(phone);
        profile.setCountryCode(countryCode);
        profile.setActivityType(activityType);
        profile.setSector(sector);

        return dao.update(profile);
    }

    // ─── تحديث الرصيد والتاريخ ─────────────────────────
    public boolean updateBalanceAndDate(double newBalance, String newOpenDate) {
        if (!dao.exists()) return false;
        return dao.updateBalanceAndDate(newBalance, newOpenDate);
    }

    // ─── حذف الحساب ────────────────────────────────────
    public boolean deleteProfile() {
        if (!dao.exists()) return false;
        return dao.delete();
    }

    // ─── هل يوجد مستخدم؟ ───────────────────────────────
    public boolean profileExists() {
        return dao.exists();
    }
}
