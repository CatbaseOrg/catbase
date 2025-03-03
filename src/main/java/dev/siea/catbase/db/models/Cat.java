package dev.siea.catbase.db.models;

import java.util.UUID;

public class Cat {
    private int id;
    private String title;
    private String description;
    private int author;

    public Cat(int id, String title, String description, int creator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreator() {
        return author;
    }

    public void setCreator(int creator) {
        this.author = creator;
    }
}
