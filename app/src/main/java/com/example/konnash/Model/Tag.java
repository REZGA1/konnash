package com.example.konnash.Model;

public class Tag {
    private long id;
    private String name;
    private String color; // Hex color code like "#3498DB"
    private int clientCount;
    private int fournisseurCount;

    public Tag() {
        this.clientCount = 0;
        this.fournisseurCount = 0;
    }

    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
        this.clientCount = 0;
        this.fournisseurCount = 0;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public int getFournisseurCount() {
        return fournisseurCount;
    }

    public void setFournisseurCount(int fournisseurCount) {
        this.fournisseurCount = fournisseurCount;
    }
}
