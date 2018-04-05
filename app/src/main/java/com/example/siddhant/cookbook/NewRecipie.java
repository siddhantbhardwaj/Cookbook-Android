package com.example.siddhant.cookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class NewRecipie extends AppCompatActivity {
    EditText title, instrunction, ingredients, link, description;

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
        setContentView(R.layout.activity_new_recipie);

        title = (EditText) findViewById(R.id.title);
        instrunction = (EditText) findViewById(R.id.instruction);
        description = (EditText) findViewById(R.id.description);
        ingredients = (EditText) findViewById(R.id.ingredients);
        link = (EditText) findViewById(R.id.link);
        Button btnCreate = (Button) findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipie(title.getText().toString(), description.getText().toString(), ingredients.getText().toString(), instrunction.getText().toString(), link.getText().toString());
            }
        });
    }

    private void createRecipie(String title, String description, String ingredients, String instruction, String link) {
        final User user = new User(this);

        String url = Config.getInstance().getApiUrl() + "/recipies";
        JSONObject recipie = new JSONObject();
        JSONObject recipieInfo = new JSONObject();
        try {
            recipieInfo.put("title", title);
            recipieInfo.put("description", description);
            recipieInfo.put("ingredients", ingredients);
            recipieInfo.put("instruction", instruction);
            recipieInfo.put("link", link);

            recipie.put("recipie", recipieInfo);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .addJSONObjectBody(recipie)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent i = new Intent(NewRecipie.this, RecipiesActivity.class);
                        startActivity(i);
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
