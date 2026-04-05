package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private final DatabaseHelper dbHelper;

    public TransactionDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // GET ALL ACTIVE — المعاملات النشطة فقط
    public List<Transaction> getActive() {
        SQLiteDatabase    db     = dbHelper.getReadableDatabase();
        Cursor            cursor = db.rawQuery(
                "SELECT * FROM transactions WHERE is_archived = 0 ORDER BY date_time DESC", null);
        List<Transaction> list   = new ArrayList<>();

        while (cursor.moveToNext()) list.add(cursorToModel(cursor));

        cursor.close();
        return list;
    }

    // GET BY ARCHIVE ID — كل معاملات أرشيف معين
    public List<Transaction> getByArchiveId(long archiveId) {
        SQLiteDatabase    db     = dbHelper.getReadableDatabase();
        Cursor            cursor = db.rawQuery(
                "SELECT * FROM transactions WHERE archive_id = ? ORDER BY date_time DESC",
                new String[]{String.valueOf(archiveId)});
        List<Transaction> list   = new ArrayList<>();

        while (cursor.moveToNext()) list.add(cursorToModel(cursor));

        cursor.close();
        return list;
    }

    // GET BY ID
    public Transaction getById(long id) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = db.rawQuery(
                "SELECT * FROM transactions WHERE id = ?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Transaction t = cursorToModel(cursor);
            cursor.close();
            return t;
        }

        cursor.close();
        return null;
    }

    // INSERT
    public boolean insert(Transaction transaction) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(transaction);

        return db.insert("transactions", null, values) != -1;
    }

    // UPDATE
    public boolean update(Transaction transaction) {
        SQLiteDatabase db     = dbHelper.getWritableDatabase();
        ContentValues  values = toContentValues(transaction);

        return db.update("transactions", values, "id=?",
                new String[]{String.valueOf(transaction.getId())}) > 0;
    }

    // DELETE ONE
    public boolean delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("transactions", "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    // DELETE ALL — عند حذف الحساب
    public boolean deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("transactions", null, null) > 0;
    }

    // ───── Helpers ─────

    private Transaction cursorToModel(Cursor cursor) {
        return new Transaction(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getLong(cursor.getColumnIndexOrThrow("archive_id")),
                Transaction.Type.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("type"))),
                cursor.getDouble(cursor.getColumnIndexOrThrow("amount")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getString(cursor.getColumnIndexOrThrow("image_path")),
                cursor.getString(cursor.getColumnIndexOrThrow("category")),
                cursor.getLong(cursor.getColumnIndexOrThrow("date_time")),
                cursor.getInt(cursor.getColumnIndexOrThrow("is_archived")) == 1
        );
    }

    private ContentValues toContentValues(Transaction t) {
        ContentValues values = new ContentValues();
        values.put("archive_id",  t.getArchiveId());
        values.put("type",        t.getType().name());
        values.put("amount",      t.getAmount());
        values.put("description", t.getDescription());
        values.put("image_path",  t.getImagePath());
        values.put("category",    t.getCategory());
        values.put("date_time",   t.getDateTime());
        values.put("is_archived", t.isArchived() ? 1 : 0);
        return values;
    }


    // GET BY DATE RANGE — نشطة ومؤرشفة معاً
    public List<Transaction> getByDateRange(long startDate, long endDate) {
        SQLiteDatabase    db     = dbHelper.getReadableDatabase();
        Cursor            cursor = db.rawQuery(
                "SELECT * FROM transactions WHERE date_time BETWEEN ? AND ? ORDER BY date_time DESC",
                new String[]{String.valueOf(startDate), String.valueOf(endDate)});
        List<Transaction> list   = new ArrayList<>();

        while (cursor.moveToNext()) list.add(cursorToModel(cursor));

        cursor.close();
        return list;
    }

}