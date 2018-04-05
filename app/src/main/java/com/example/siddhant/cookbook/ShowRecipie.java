package com.example.siddhant.cookbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowRecipie extends AppCompatActivity {
    TextView title, description, ingredients, instruction, link;
    private int recipie_id;
    private static RatingBar ratingBar;

    // Code referred from https://stackoverflow.com/a/31387193/2974803
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_recipie:
                editRecipie();
                return true;
            case R.id.action_delete_recipie:
                deleteAction();
                return true;
            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipie);

        title = (TextView) findViewById(R.id.title);
        instruction = (TextView) findViewById(R.id.instruction);
        description = (TextView) findViewById(R.id.description);
        ingredients = (TextView) findViewById(R.id.ingredients);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        link = (TextView) findViewById(R.id.link);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            this.recipie_id = bundle.getInt("id");
            getRecipie(this.recipie_id);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                updateRating(rating);
            }
        });
    }

    public void updateRating(float rating) {
        final User user = new User(this);
        JSONObject recipie = new JSONObject();
        JSONObject recipieInfo = new JSONObject();
        try {
            recipieInfo.put("rating", rating);
            recipie.put("recipie", recipieInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Config.getInstance().getApiUrl() + "/recipies/" + this.recipie_id;
        AndroidNetworking.patch(url)
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .addJSONObjectBody(recipie)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ShowRecipie.this, "Rating of recipie is: " + response.get("rating").toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                      map through each of error.errorBody.errors, and append inline errors
                        Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getRecipie(Integer id) {
        final User user = new User(this);
        String url = Config.getInstance().getApiUrl() + "/recipies/" + id;
        AndroidNetworking.get(url)
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        renderRecipie(response);
                    }
                    @Override
                    public void onError(ANError error) {
//                      map through each of error.errorBody.errors, and append inline errors
                        Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void renderRecipie(JSONObject response) {
        try{
            title.setText(response.get("title").toString());
            description.setText(response.get("description").toString());
            instruction.setText(response.get("instruction").toString());
            ingredients.setText(response.get("ingredients").toString());
            link.setText(response.get("link").toString());
            ratingBar.setRating(Float.parseFloat(response.get("rating").toString()));
        } catch (JSONException e) {

        }

    }

    public void editRecipie() {
        // Move the screen to new recipie with the information
        Intent intent = new Intent(ShowRecipie.this, NewRecipie.class);
        intent.putExtra("id", this.recipie_id);
        startActivity(intent);
        finish();
    }

    public void deleteAction() {
        // Code referred from https://stackoverflow.com/a/5127506/2974803
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to delete this recipie?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteRecipie();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void deleteRecipie() {
        final User user = new User(this);
        String url = Config.getInstance().getApiUrl() + "/recipies/" + this.recipie_id;
        AndroidNetworking.delete(url)
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(ShowRecipie.this, RecipiesActivity.class);
                        // Code referred from https://stackoverflow.com/a/13690875/2974803
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
//                      map through each of error.errorBody.errors, and append inline errors
                        Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
