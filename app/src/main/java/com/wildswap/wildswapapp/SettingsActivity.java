package com.wildswap.wildswapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    public SettingsActivity() {
    }

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    TextView language;
    CheckBox normalMap;
    CheckBox terrainMap;
    CheckBox hybridMap;
    CheckBox satelliteMap;
    Button btnLogout;

    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //View rootView = inflater.inflate(R.layout.activity_settings, container, false);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        language = (TextView) findViewById(R.id.language);
        normalMap = (CheckBox) findViewById(R.id.normalMap);
        terrainMap = (CheckBox) findViewById(R.id.terrainMap);
        hybridMap = (CheckBox) findViewById(R.id.hybridMap);
        satelliteMap = (CheckBox) findViewById(R.id.satelliteMap);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        language.setText(Locale.getDefault().getDisplayLanguage());

        String mapType = AppController.getString(this, "mapType");

        if(mapType.equals("normal")){
            normalMap.setChecked(true);

        } else if(mapType.equals("terrain")){
            terrainMap.setChecked(true);

        } else if(mapType.equals("hybrid")){
            hybridMap.setChecked(true);

        } else if(mapType.equals("satellite")){
            satelliteMap.setChecked(true);
        }


        terrainMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    AppController.setString(SettingsActivity.this, "mapType", "terrain");
                    normalMap.setChecked(false);
                    hybridMap.setChecked(false);
                    satelliteMap.setChecked(false);
                }
            }
        });

        normalMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    AppController.setString(SettingsActivity.this, "mapType", "normal");
                    terrainMap.setChecked(false);
                    hybridMap.setChecked(false);
                    satelliteMap.setChecked(false);
                }
            }
        });

        hybridMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    AppController.setString(SettingsActivity.this, "mapType", "hybrid");
                    terrainMap.setChecked(false);
                    normalMap.setChecked(false);
                    satelliteMap.setChecked(false);
                }
            }
        });

        satelliteMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    AppController.setString(SettingsActivity.this, "mapType", "satellite");
                    terrainMap.setChecked(false);
                    normalMap.setChecked(false);
                    hybridMap.setChecked(false);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
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
                Intent intent = new Intent(this, MainActivity_Spinner.class);
                intent.putExtra("data", false);
                startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}