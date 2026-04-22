package com.example.konnash.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "konnash.db";
    private static final int    DB_VERSION = 5;

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE user_profile (" +
                        "id            INTEGER PRIMARY KEY CHECK(id = 1)," +
                        "store_name    TEXT NOT NULL," +
                        "phone         TEXT NOT NULL," +
                        "country_code  TEXT NOT NULL," +
                        "activity_type TEXT," +
                        "sector        TEXT," +
                        "income        REAL DEFAULT 0," +
                        "expense       REAL DEFAULT 0," +
                        "open_date     INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE app_settings (" +
                        "id       INTEGER PRIMARY KEY CHECK(id = 1)," +
                        "language TEXT NOT NULL DEFAULT 'ar'," +
                        "pin_code TEXT)"
        );

        db.execSQL(
                "CREATE TABLE business_card (" +
                        "id                   INTEGER PRIMARY KEY CHECK(id = 1)," +
                        "personal_name        TEXT," +
                        "store_name           TEXT," +
                        "phone                TEXT," +
                        "business_description TEXT," +
                        "address              TEXT," +
                        "city                 TEXT)"
        );

        db.execSQL(
                "CREATE TABLE transaction_archives (" +
                        "id         INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "open_date  INTEGER NOT NULL," +
                        "close_date INTEGER NOT NULL," +
                        "count      INTEGER DEFAULT 0," +
                        "income     REAL DEFAULT 0," +
                        "expense    REAL DEFAULT 0)"
        );

        db.execSQL(
                "CREATE TABLE transactions (" +
                        "id          INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "archive_id  INTEGER DEFAULT 0," + // 0 = نشطة
                        "type        TEXT NOT NULL," +
                        "amount      REAL NOT NULL," +
                        "description TEXT," +
                        "image_path  TEXT," +
                        "category    TEXT," +
                        "date_time   INTEGER NOT NULL," +
                        "is_archived INTEGER DEFAULT 0)"
        );

        db.execSQL(
                "CREATE TABLE categories (" +
                        "id   INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL UNIQUE)"
        );

        db.execSQL(
                "CREATE TABLE clients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "phone TEXT," +
                        "address TEXT)"
        );

        db.execSQL(
                "CREATE TABLE fournisseurs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "phone TEXT," +
                        "address TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS transaction_archives");
        db.execSQL("DROP TABLE IF EXISTS business_card");
        db.execSQL("DROP TABLE IF EXISTS app_settings");
        db.execSQL("DROP TABLE IF EXISTS user_profile");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS clients");
        db.execSQL("DROP TABLE IF EXISTS fournisseurs");
        onCreate(db);
    }
}