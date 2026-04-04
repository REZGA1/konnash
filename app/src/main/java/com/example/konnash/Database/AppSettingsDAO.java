package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.AppSettings;

public class AppSettingsDAO {

    private final DatabaseHelper dbHelper;

    public AppSettingsDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // GET
    public AppSettings get() {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = db.rawQuery("SELECT * FROM app_settings LIMIT 1", null);

        if (cursor.moveToFirst()) {
            AppSettings settings = new AppSettings(
                    cursor.getString(cursor.getColumnIndexOrThrow("language")),
                    cursor.getString(cursor.getColumnIndexOrThrow("pin_code"))
            );
            cursor.close();
            return settings;
        }

        cursor.close();
        return null;
    }

    // INSERT
    public boolean insert(AppSettings settings) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(settings);
        values.put("id", 1);

        return db.insert("app_settings", null, values) != -1;
    }

    // UPDATE
    public boolean update(AppSettings settings) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(settings);

        return db.update("app_settings", values, "id=1", null) > 0;
    }

    // SAVE (insert أو update تلقائياً)
    public boolean save(AppSettings settings) {
        return get() == null ? insert(settings) : update(settings);
    }

    // DELETE
    public boolean delete() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("app_settings", "id=1", null) > 0;
    }

    // ───── Helper ─────
    private ContentValues toContentValues(AppSettings settings) {
        ContentValues values = new ContentValues();
        values.put("language", settings.getLanguage());
        values.put("pin_code", settings.getPinCode()); // يقبل null
        return values;
    }


}