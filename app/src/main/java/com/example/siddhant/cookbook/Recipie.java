package com.example.siddhant.cookbook;

import android.content.Intent;

/**
 * Created by siddhant on 3/22/18.
 */

public class Recipie {
    private Integer id;
    private String title;
    private String description;
    private String instruction;
    private String ingredients;
    private String link;

    public Recipie(Integer id, String title, String description, String instruction, String ingredients, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instruction = instruction;
        this.ingredients = ingredients;
        this.link = link;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
