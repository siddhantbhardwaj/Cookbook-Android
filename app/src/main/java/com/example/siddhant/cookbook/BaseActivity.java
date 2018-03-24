package com.example.siddhant.cookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

/**
 * Created by siddhant on 3/24/18.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                signout();
                return true;

            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void signout() {
        final Intent i = new Intent(this, LoginActivity.class);
        final User user = new User(this);
        AndroidNetworking
                .get(Config.getInstance().getApiUrl() + "/logout")
                .addHeaders("X-Cookbook-Auth", user.getAuthToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
        user.destroySession();
        startActivity(i);
        finish();
    }
}
