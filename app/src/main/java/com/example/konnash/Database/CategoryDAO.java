package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private final DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // GET ALL
    public List<Category> getAll() {
        SQLiteDatabase  db     = dbHelper.getReadableDatabase();
        Cursor          cursor = db.rawQuery(
                "SELECT * FROM categories ORDER BY name ASC", null);
        List<Category>  list   = new ArrayList<>();

        while (cursor.moveToNext()) list.add(cursorToModel(cursor));

        cursor.close();
        return list;
    }

    // INSERT
    public boolean insert(Category category) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put("name", category.getName());

        return db.insert("categories", null, values) != -1;
    }

    // UPDATE
    public boolean update(Category category) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put("name", category.getName());

        return db.update("categories", values, "id=?",
                new String[]{String.valueOf(category.getId())}) > 0;
    }

    // DELETE
    public boolean delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("categories", "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    // DELETE ALL — عند حذف الحساب
    public boolean deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("categories", null, null) > 0;
    }

    // ───── Helpers ─────

    private Category cursorToModel(Cursor cursor) {
        return new Category(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name"))
        );
    }
}