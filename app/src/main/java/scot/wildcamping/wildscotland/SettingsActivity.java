package scot.wildcamping.wildscotland;

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

import scot.wildcamping.wildscotland.Objects.StoredData;
import scot.wildcamping.wildscotland.Objects.User;

public class SettingsActivity extends AppCompatActivity {

    public SettingsActivity() {
    }

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    TextView txtName;
    TextView txtEmail;
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

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        txtName.setText(thisUser.getName());
        txtEmail.setText(thisUser.getEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        TextView versionText = (TextView) findViewById(R.id.versionName);
        versionText.setText("Version: " + version);
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

