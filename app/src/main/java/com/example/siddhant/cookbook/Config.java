package com.example.siddhant.cookbook;

/**
 * Created by siddhant on 2/24/18.
 */

class Config {
    private static final Config ourInstance = new Config();

    static Config getInstance() {
        return ourInstance;
    }

    private Config() {
    }

    private String apiUrl = "https://cookbook-rails.herokuapp.com";
    public String getApiUrl() {
        return apiUrl;
    }
}
