package com.example.konnash.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "konnash.db";
    private static final int DB_VERSION = 1;

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
                        "id INTEGER PRIMARY KEY CHECK(id = 1)," +
                        "store_name TEXT NOT NULL," +
                        "phone TEXT NOT NULL," +
                        "country_code TEXT NOT NULL," +
                        "activity_type TEXT," +
                        "sector TEXT," +
                        "income  REAL DEFAULT 0," +
                        "expense REAL DEFAULT 0," +
                        "open_date INTEGER)"
        );


        db.execSQL(
                "CREATE TABLE app_settings (" +
                        "id INTEGER PRIMARY KEY CHECK(id = 1)," +
                        "language TEXT NOT NULL DEFAULT 'ar'," +
                        "pin_code TEXT)"
        );

        db.execSQL(
                "CREATE TABLE business_card (" +
                        "id INTEGER PRIMARY KEY CHECK(id = 1)," +
                        "personal_name TEXT," +
                        "store_name TEXT," +
                        "phone TEXT," +
                        "business_description TEXT," +
                        "address TEXT," +
                        "city TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user_profile");
        db.execSQL("DROP TABLE IF EXISTS app_settings");
        db.execSQL("DROP TABLE IF EXISTS business_card");
        onCreate(db);
    }
}