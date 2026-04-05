package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.TransactionArchive;

import java.util.ArrayList;
import java.util.List;

public class TransactionArchiveDAO {

    private final DatabaseHelper dbHelper;

    public TransactionArchiveDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // GET ALL
    public List<TransactionArchive> getAll() {
        SQLiteDatabase          db     = dbHelper.getReadableDatabase();
        Cursor                  cursor = db.rawQuery(
                "SELECT * FROM transaction_archives ORDER BY close_date DESC", null);
        List<TransactionArchive> list  = new ArrayList<>();

        while (cursor.moveToNext()) list.add(cursorToModel(cursor));

        cursor.close();
        return list;
    }

    // GET BY ID
    public TransactionArchive getById(long id) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = db.rawQuery(
                "SELECT * FROM transaction_archives WHERE id=?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            TransactionArchive archive = cursorToModel(cursor);
            cursor.close();
            return archive;
        }

        cursor.close();
        return null;
    }

    // INSERT — يرجع الـ id الجديد
    public long insert(TransactionArchive archive) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(archive);

        return db.insert("transaction_archives", null, values);
    }

    // UPDATE
    public boolean update(TransactionArchive archive) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(archive);

        return db.update("transaction_archives", values, "id=?",
                new String[]{String.valueOf(archive.getId())}) > 0;
    }

    // DELETE ONE
    public boolean delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("transaction_archives", "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    // DELETE ALL — عند حذف الحساب
    public boolean deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("transaction_archives", null, null) > 0;
    }

    // ───── Helpers ─────

    private TransactionArchive cursorToModel(Cursor cursor) {
        return new TransactionArchive(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getLong(cursor.getColumnIndexOrThrow("open_date")),
                cursor.getLong(cursor.getColumnIndexOrThrow("close_date")),
                cursor.getInt(cursor.getColumnIndexOrThrow("count")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("income")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("expense"))
        );
    }

    private ContentValues toContentValues(TransactionArchive archive) {
        ContentValues values = new ContentValues();
        values.put("open_date",  archive.getOpenDate());
        values.put("close_date", archive.getCloseDate());
        values.put("count",      archive.getCount());
        values.put("income",     archive.getIncome());
        values.put("expense",    archive.getExpense());
        return values;
    }
}