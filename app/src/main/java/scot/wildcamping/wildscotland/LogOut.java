package scot.wildcamping.wildscotland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.StoredUsers;
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.Objects.knownSite;

public class LogOut extends AppCompatActivity {

    TextView txtName;
    TextView txtEmail;
    Button btnLogout;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        String name = AppController.getString(getApplicationContext(), "name");
        String email = AppController.getString(getApplicationContext(), "email"); //email is currently blank so print uid instead
        txtName.setText(name);
        txtEmail.setText(email);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }


    private void logoutUser() {
        session.setLogin(false);
        knownSite inst = new knownSite();
        SparseArray<Site> ownedSites = inst.getOwnedSitesMap();
        ownedSites.clear();
        AppController.setString(this, "name", "");
        AppController.setString(this, "email", "");
        AppController.setString(this, "bio", "");
        AppController.setString(this, "profile_pic", "null");
        AppController.setString(this, "cover_pic", "null");
        inst.setOwnedSitesMap(ownedSites);
        Intent intent = new Intent(LogOut.this, Login.class);
        startActivity(intent);
        finish();
    }
}
