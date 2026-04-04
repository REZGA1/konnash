package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.BusinessCard;

public class BusinessCardDAO {

    private final DatabaseHelper dbHelper;

    public BusinessCardDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // GET
    public BusinessCard get() {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = db.rawQuery("SELECT * FROM business_card LIMIT 1", null);

        if (cursor.moveToFirst()) {
            BusinessCard card = cursorToModel(cursor);
            cursor.close();
            return card;
        }

        cursor.close();
        return null;
    }

    // INSERT
    public boolean insert(BusinessCard card) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(card);
        values.put("id", 1);

        return db.insert("business_card", null, values) != -1;
    }

    // UPDATE
    public boolean update(BusinessCard card) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(card);

        return db.update("business_card", values, "id=1", null) > 0;
    }

    // SAVE
    public boolean save(BusinessCard card) {
        return get() == null ? insert(card) : update(card);
    }

    // DELETE (تفريغ البطاقة بالكامل)
    public boolean delete() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("business_card", "id=1", null) > 0;
    }

    // ───── Helpers ─────

    private BusinessCard cursorToModel(Cursor cursor) {
        return new BusinessCard(
                cursor.getString(cursor.getColumnIndexOrThrow("personal_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("store_name")),
                cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                cursor.getString(cursor.getColumnIndexOrThrow("business_description")),
                cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cursor.getString(cursor.getColumnIndexOrThrow("city"))
        );
    }

    private ContentValues toContentValues(BusinessCard card) {
        ContentValues values = new ContentValues();
        values.put("personal_name",        card.getPersonalName());
        values.put("store_name",           card.getStoreName());
        values.put("phone",                card.getPhone());
        values.put("business_description", card.getBusinessDescription());
        values.put("address",              card.getAddress());
        values.put("city",                 card.getCity());
        return values;
    }
}