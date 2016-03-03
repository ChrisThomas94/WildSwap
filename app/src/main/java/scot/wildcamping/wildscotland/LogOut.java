package scot.wildcamping.wildscotland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        Intent intent = new Intent(LogOut.this, Login.class);
        startActivity(intent);
        finish();
    }
}
