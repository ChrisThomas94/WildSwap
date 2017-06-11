package com.wildswap.wildswapapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchKnownSites;
import com.wildswap.wildswapapp.AsyncTask.FetchTradeRequests;
import com.wildswap.wildswapapp.AsyncTask.FetchUnknownSites;
import com.wildswap.wildswapapp.AsyncTask.FetchUsers;
import com.wildswap.wildswapapp.AsyncTask.Login;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 31-Mar-16.
 *
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager(getApplicationContext());

        StoredData inst = new StoredData();
        final User thisUser = inst.getLoggedInUser();
        final Boolean showDialog = false;

        //If the session is logged in move to MainActivity
        if (session.isLoggedIn()) {

            System.out.println("session editor "+session.editor.toString());

            System.out.println("user" + thisUser.getEmail());
            thisUser.setEmail(AppController.getString(this, "email"));

            ArrayList<String> users = new ArrayList<>();
            users.add(0, thisUser.getEmail());

            final Intent intent = new Intent(this, MainActivity_Spinner.class);
            intent.putExtra("fetchData", false);

            if(isNetworkAvailable()) {
                new FetchUsers(this, users, showDialog, new AsyncResponse() {

                    @Override
                    public void processFinish(String output) {
                        new FetchTradeRequests(SplashActivity.this, showDialog, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                new FetchKnownSites(SplashActivity.this, showDialog, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        new FetchUnknownSites(SplashActivity.this, showDialog, new AsyncResponse() {
                                            @Override
                                            public void processFinish(String output) {
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).execute();

                                    }
                                }).execute();
                            }
                        }).execute();
                    }
                }).execute();
            } else {

                /*thisUser.setEmail(AppController.getString(this, "email"));
                thisUser.setUid(AppController.getString(this, "uid"));
                thisUser.setUserType(AppController.getString(this, "userType"));
                thisUser.setBio(AppController.getString(this, "bio"));
                thisUser.setWhy(AppController.getString(this, "why"));
                thisUser.setCover_pic(AppController.getString(this, "cover_pic"));
                thisUser.setProfile_pic(AppController.getString(this, "profile_pic"));
                thisUser.setCountry(AppController.getString(this, "country"));
                */

                Intent i = new Intent(this, Login.class);
                startActivity(i);
                finish();
            }
        } else {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
