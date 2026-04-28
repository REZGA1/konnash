package com.example.konnash.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.konnash.Model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    private SQLiteDatabase db;

    public TagDAO(Context context) {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    // Create tags table
    public static void createTable(SQLiteDatabase db) {
        String CREATE_TAGS_TABLE = "CREATE TABLE IF NOT EXISTS tags (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE," +
                "color TEXT," +
                "client_count INTEGER DEFAULT 0," +
                "fournisseur_count INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TAGS_TABLE);
    }

    // Add a new tag
    public long addTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("name", tag.getName());
        values.put("color", tag.getColor());
        values.put("client_count", tag.getClientCount());
        values.put("fournisseur_count", tag.getFournisseurCount());
        return db.insert("tags", null, values);
    }

    // Get all tags
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        Cursor cursor = db.query("tags", null, null, null, null, null, "id DESC");
        
        while (cursor.moveToNext()) {
            Tag tag = new Tag();
            tag.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            tag.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            tag.setColor(cursor.getString(cursor.getColumnIndexOrThrow("color")));
            tag.setClientCount(cursor.getInt(cursor.getColumnIndexOrThrow("client_count")));
            tag.setFournisseurCount(cursor.getInt(cursor.getColumnIndexOrThrow("fournisseur_count")));
            tags.add(tag);
        }
        cursor.close();
        return tags;
    }

    // Delete a tag
    public void deleteTag(long id) {
        db.delete("tags", "id = ?", new String[]{String.valueOf(id)});
    }

    // Update tag counts (when assigning to clients/fournisseurs)
    public void updateTagCounts(long tagId, int clientCount, int fournisseurCount) {
        ContentValues values = new ContentValues();
        values.put("client_count", clientCount);
        values.put("fournisseur_count", fournisseurCount);
        db.update("tags", values, "id = ?", new String[]{String.valueOf(tagId)});
    }

    // Check if tag exists by name
    public boolean tagExists(String name) {
        Cursor cursor = db.query("tags", null, "name = ?", new String[]{name}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
