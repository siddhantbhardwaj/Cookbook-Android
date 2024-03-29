package com.example.siddhant.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipiesActivity extends BaseActivity {
    private RecipieListAdapter recipieListAdapter;

    // Code referred from https://stackoverflow.com/a/31387193/2974803
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Code referred from https://www.youtube.com/watch?v=4RfvmrelFzE
        MenuItem menuItem = menu.findItem(R.id.search_badge);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recipieListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipies);
        loadRecipies();
    }

    public void renderRecipies(JSONArray recipies) {
        final ListView recipiesListView;
        recipiesListView = findViewById(R.id.recipies);

        ArrayList<Recipie> recipiesList = new ArrayList<>();
        for(int i = 0; i < recipies.length(); i++){
            try {
                JSONObject recipie = recipies.getJSONObject(i);
                Recipie r = new Recipie(recipie.getInt("id"), recipie.getString("title"), recipie.getString("description"), "", "", "");
                recipiesList.add(r);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recipieListAdapter = new RecipieListAdapter(RecipiesActivity.this, R.layout.recipie_item, recipiesList);
        recipiesListView.setAdapter(recipieListAdapter);
        recipiesListView.setEmptyView(findViewById(R.id.emptyElement));

        recipiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecipiesActivity.this, ShowRecipie.class);
                Recipie r = (Recipie) recipiesListView.getItemAtPosition(position);
                intent.putExtra("id", r.getId());
                startActivity(intent);
            }
        });
    }

    public void loadRecipies() {
        final User user = new User(this);

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

    public void goToCreateNew(View view) {
        Intent intent = new Intent(this, NewRecipie.class);
        startActivity(intent);
    }
}
