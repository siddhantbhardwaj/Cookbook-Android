package com.example.siddhant.cookbook;

/**
 * Created by siddhant on 3/22/18.
 */

public class Recipie {
    private String title;
    private String description;
    private String instruction;
    private String ingredients;

    public Recipie(String title, String description, String instruction, String ingredients) {
        this.title = title;
        this.description = description;
        this.instruction = instruction;
        this.ingredients = ingredients;
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
}
