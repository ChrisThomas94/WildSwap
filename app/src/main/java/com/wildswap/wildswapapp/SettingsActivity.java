package com.wildswap.wildswapapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.CreateSite;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    public SettingsActivity() {
    }

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    TextView txtEmail;
    TextView language;
    TextView about;
    TextView projects;
    TextView credits;
    Button btnLogout;
    Button btnDeactivate;

    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //View rootView = inflater.inflate(R.layout.activity_settings, container, false);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        txtEmail = (TextView) findViewById(R.id.email);
        language = (TextView) findViewById(R.id.language);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnDeactivate = (Button) findViewById(R.id.btnDeactivate);
        about = (TextView) findViewById(R.id.about);
        projects = (TextView) findViewById(R.id.projects);
        credits = (TextView) findViewById(R.id.credits);

        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        txtEmail.setText(thisUser.getEmail());

        language.setText(Locale.getDefault().getDisplayLanguage());

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingsActivity.this);
                builder1.setTitle("Are you sure?!");
                builder1.setMessage("Do you really wish to deactivate your account?");

                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert1 = builder1.create();
                alert1.show();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(about);
            }
        });

        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent projects = new Intent(getBaseContext(), ProjectsActivity.class);
                startActivity(projects);
            }
        });
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent credits = new Intent(getBaseContext(), AcknowledgementsActivity.class);
                startActivity(credits);
            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}