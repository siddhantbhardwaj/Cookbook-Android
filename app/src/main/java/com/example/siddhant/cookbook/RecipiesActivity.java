package com.example.siddhant.cookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RecipiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);
    }

    public void logout(View view) {
        new User(RecipiesActivity.this).destroySession();
        Intent i = new Intent(RecipiesActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
