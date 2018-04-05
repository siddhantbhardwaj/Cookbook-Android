package com.example.siddhant.cookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    EditText title, instruction, ingredients, link, description;
    private boolean isEdit = false;
    private int recipie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipie);

        title = (EditText) findViewById(R.id.title);
        instruction = (EditText) findViewById(R.id.instruction);
        description = (EditText) findViewById(R.id.description);
        ingredients = (EditText) findViewById(R.id.ingredients);
        link = (EditText) findViewById(R.id.link);
        Button btnCreate = (Button) findViewById(R.id.btnCreate);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Integer id = extras.getInt("id");
            if (id != null) {
                this.isEdit = true;
                this.recipie_id = id;
                getRecipieAndUpdateFields(this.recipie_id);
            }
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipie(title.getText().toString(), description.getText().toString(), ingredients.getText().toString(), instruction.getText().toString(), link.getText().toString());
            }
        });
    }

    private void createRecipie(String title, String description, String ingredients, String instruction, String link) {
        final User user = new User(this);
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


        String url = Config.getInstance().getApiUrl() + "/recipies";
        if (this.isEdit) {
            url = url + "/" + this.recipie_id;
        }

        (this.isEdit ? AndroidNetworking.patch(url) : AndroidNetworking.post(url))
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .addJSONObjectBody(recipie)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        returnToAppropriateActivity();
                    }
                    @Override
                    public void onError(ANError error) {
//                      map through each of error.errorBody.errors, and append inline errors
                        Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void returnToAppropriateActivity() {
        Intent i = (this.isEdit
                ? (new Intent(NewRecipie.this, ShowRecipie.class))
                : (new Intent(NewRecipie.this, RecipiesActivity.class)));
        if (this.isEdit) {
            i.putExtra("id", this.recipie_id);
        }
        startActivity(i);
    }

    public void getRecipieAndUpdateFields(Integer id) {
        final User user = new User(this);
        String url = Config.getInstance().getApiUrl() + "/recipies/" + id.toString();
        AndroidNetworking.get(url)
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateFields(response);
                    }
                    @Override
                    public void onError(ANError error) {
//                      map through each of error.errorBody.errors, and append inline errors
                        Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void populateFields(JSONObject recipie) {
        try{
            title.setText(recipie.get("title").toString());
            description.setText(recipie.get("description").toString());
            instruction.setText(recipie.get("instruction").toString());
            ingredients.setText(recipie.get("ingredients").toString());
            link.setText(recipie.get("link").toString());
        } catch (JSONException e) {

        }
    }

}
