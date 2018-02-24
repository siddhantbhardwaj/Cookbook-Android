package com.example.siddhant.cookbook;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by siddhant on 2/24/18.
 * Code referred from https://www.youtube.com/watch?v=B8q8DJzFhC0
 */

public class User {
    SharedPreferences sharedPreferences;
    public void destroySession() {
        sharedPreferences.edit().clear().commit();
    }

    public String getName() {
        name = sharedPreferences.getString("userName", "");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("userName", name).commit();
    }

    public String getEmail() {
        email = sharedPreferences.getString("userEmail", "");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("userEmail", email).commit();
    }

    public String getAuthToken() {
        authToken = sharedPreferences.getString("authToken", "");
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        sharedPreferences.edit().putString("authToken", authToken).commit();
    }

    private String name;
    private String email;
    private String authToken;
    Context context;

    public User(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
    };
}
