package com.example.konnash.Model;

public class Category {

    private long   id;
    private String name;

    public Category(long id, String name) {
        this.id   = id;
        this.name = name;
    }

    // Getters
    public long   getId()   { return id;   }
    public String getName() { return name; }

    // Setters
    public void setName(String name) { this.name = name; }
}