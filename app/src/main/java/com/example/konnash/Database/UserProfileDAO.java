package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.UserProfile;

public class UserProfileDAO {

    private final DatabaseHelper dbHelper;

    public UserProfileDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // GET
    public UserProfile get() {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = db.rawQuery("SELECT * FROM user_profile LIMIT 1", null);

        if (cursor.moveToFirst()) {
            UserProfile profile = cursorToModel(cursor);
            cursor.close();
            return profile;
        }

        cursor.close();
        return null;
    }

    // INSERT
    public boolean insert(UserProfile profile) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(profile);
        values.put("id", 1);

        return db.insert("user_profile", null, values) != -1;
    }

    // UPDATE
    public boolean update(UserProfile profile) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(profile);

        return db.update("user_profile", values, "id=1", null) > 0;
    }

    // SAVE
    public boolean save(UserProfile profile) {
        return get() == null ? insert(profile) : update(profile);
    }

    // DELETE
    public boolean delete() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("user_profile", "id=1", null) > 0;
    }

    // ───── Helpers ─────

    private UserProfile cursorToModel(Cursor cursor) {
        return new UserProfile(
                cursor.getString(cursor.getColumnIndexOrThrow("store_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                cursor.getString(cursor.getColumnIndexOrThrow("country_code")),
                cursor.getString(cursor.getColumnIndexOrThrow("activity_type")),
                cursor.getString(cursor.getColumnIndexOrThrow("sector")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("income")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("expense")),
                cursor.getLong(cursor.getColumnIndexOrThrow("open_date"))
        );
    }

    private ContentValues toContentValues(UserProfile profile) {
        ContentValues values = new ContentValues();
        values.put("store_name",    profile.getStoreName());
        values.put("phone",         profile.getPhone());
        values.put("country_code",  profile.getCountryCode());
        values.put("activity_type", profile.getActivityType());
        values.put("sector",        profile.getSector());
        values.put("income",        profile.getIncome());
        values.put("expense",       profile.getExpense());
        // لا يوجد balance لأنه يُحسب تلقائياً
        values.put("open_date",     profile.getOpenDate());
        return values;
    }
}