package com.example.siddhant.cookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipiesActivity extends BaseActivity {
    private ArrayAdapter<String> itemsAdapter;

    // Code referred from https://stackoverflow.com/a/31387193/2974803
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);

        loadRecipies();
    }

    public void renderRecipies(JSONArray recipies) {
        ListView recipiesListView;
        recipiesListView = findViewById(R.id.recipies);
        final ArrayList<Recipie> recipiesList = new ArrayList<>();
        for(int i = 0; i < recipies.length(); i++){
            try {
                JSONObject recipie = recipies.getJSONObject(i);
                Recipie r = new Recipie(recipie.getString("title"), recipie.getString("description"), "", "");
                RecipieListAdapter recipieListAdapter = new RecipieListAdapter(RecipiesActivity.this, R.layout.recipie_item, recipiesList);
                recipiesListView.setAdapter(recipieListAdapter);
                recipiesList.add(r);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadRecipies() {
        final User user = new User(this);

        // fetch recipies of this user and itemsAdapter.add(item);
        String url = Config.getInstance().getApiUrl() + "/recipies";
        AndroidNetworking
                .get(url)
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        renderRecipies(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }
}
