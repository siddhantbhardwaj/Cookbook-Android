package com.example.siddhant.cookbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button btnSignup = (Button) findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(name.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void signup(final String name, String email, String password) {
        String url = Config.getInstance().getApiUrl() + "/users";
        JSONObject user = new JSONObject();
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("password", password);

            user.put("user", userInfo);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addJSONObjectBody(user)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        User user = new User(RegisterActivity.this);
                        String name = "", authToken = "", email = "";
                        try{
                            authToken = response.get("auth_token").toString();
                            name = response.get("name").toString();
                            email= response.get("email").toString();
                        } catch (JSONException e) {

                        }
                        user.setAuthToken(authToken);
                        user.setName(name);
                        user.setEmail(email);

                        Intent i = new Intent(RegisterActivity.this, RecipiesActivity.class);
                        startActivity(i);
                        finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        String str = error.getErrorBody();
                        JSONObject errors = null;
                        try {
                            errors = new JSONObject(str);
                            showInlineErrors(errors);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void goToSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showInlineErrors(JSONObject errors) {
        try {
            JSONObject json = errors.getJSONObject("errors");
            Iterator<String> iter = json.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONArray jsonArray = (JSONArray) json.get(key);
                    String result = jsonArray.join(",");
                    switch(key) {
                        case "name":
                            name.setError(result);
                            break;
                        case "email":
                            email.setError(result);
                            break;
                        case "password":
                            password.setError(result);
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
