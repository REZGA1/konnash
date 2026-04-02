package com.example.konnash.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.UserProfile;

public class UserProfileDAO {

    private final DatabaseHelper dbHelper;
    private static final int PROFILE_ID = 1;

    public UserProfileDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // ─── INSERT ────────────────────────────────────────
    public boolean insert(UserProfile profile) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = buildContentValues(profile, true);
        long           result = db.insert("user_profile", null, values);
        return result > 0;
    }

    // ─── SELECT ────────────────────────────────────────
    public UserProfile get() {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = db.rawQuery(
                "SELECT * FROM user_profile WHERE id=?",
                new String[]{String.valueOf(PROFILE_ID)});

        if (cursor.moveToFirst()) {
            UserProfile profile = cursorToProfile(cursor);
            cursor.close();
            return profile;
        }
        cursor.close();
        return null;
    }

    // ─── UPDATE ────────────────────────────────────────
    public boolean update(UserProfile profile) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = buildContentValues(profile, false);
        int            result = db.update("user_profile", values,
                "id=?", new String[]{String.valueOf(PROFILE_ID)});
        return result > 0;
    }

    // ─── UPDATE Balance & Date ─────────────────────────
    public boolean updateBalanceAndDate(double newBalance, String newOpenDate) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put("initial_balance", newBalance);
        values.put("open_date",       newOpenDate);
        int result = db.update("user_profile", values,
                "id=?", new String[]{String.valueOf(PROFILE_ID)});
        return result > 0;
    }

    // ─── DELETE ────────────────────────────────────────
    public boolean delete() {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        int            result = db.delete("user_profile",
                "id=?", new String[]{String.valueOf(PROFILE_ID)});
        return result > 0;
    }

    // ─── EXISTS ────────────────────────────────────────
    public boolean exists() {
        return get() != null;
    }

    // ─── Helper: Cursor → UserProfile ──────────────────
    private UserProfile cursorToProfile(Cursor cursor) {
        return new UserProfile(
                cursor.getInt   (cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("store_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                cursor.getString(cursor.getColumnIndexOrThrow("country_code")),
                cursor.getString(cursor.getColumnIndexOrThrow("activity_type")),
                cursor.getString(cursor.getColumnIndexOrThrow("sector")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("initial_balance")),
                cursor.getString(cursor.getColumnIndexOrThrow("open_date"))
        );
    }

    // ─── Helper: UserProfile → ContentValues ───────────
    private ContentValues buildContentValues(UserProfile profile, boolean includeId) {
        ContentValues values = new ContentValues();
        if (includeId) values.put("id", PROFILE_ID);
        values.put("store_name",      profile.getStoreName());
        values.put("phone",           profile.getPhone());
        values.put("country_code",    profile.getCountryCode());
        values.put("activity_type",   profile.getActivityType());
        values.put("sector",          profile.getSector());
        values.put("initial_balance", profile.getInitialBalance());
        values.put("open_date",       profile.getOpenDate());
        return values;
    }
}