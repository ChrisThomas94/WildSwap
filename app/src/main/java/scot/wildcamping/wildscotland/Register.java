package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import scot.wildcamping.wildscotland.AsyncTask.AsyncResponse;
import scot.wildcamping.wildscotland.AsyncTask.CreateUser;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button tvLogin;
    TextInputLayout fullName;
    TextInputLayout emailRegister;
    TextInputLayout passwordRegister;
    EditText etFullName;
    EditText etEmailRegister;
    EditText etPasswordRegister;
    Button registerButton;
    String name;
    String token;
    String email;
    String password;
    Intent intent;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing Views
        tvLogin = (Button) findViewById(R.id.tv_signin);
        registerButton = (Button) findViewById(R.id.register_button);
        fullName = (TextInputLayout) findViewById(R.id.fullname_registerlayout);
        emailRegister = (TextInputLayout) findViewById(R.id.email_registerlayout);
        passwordRegister = (TextInputLayout) findViewById(R.id.password_registerlayout);
        etFullName = (EditText) findViewById(R.id.fullname_register);
        etEmailRegister = (EditText) findViewById(R.id.email_register);
        etPasswordRegister = (EditText) findViewById(R.id.password_register);

        tvLogin.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in
        if (session.isLoggedIn()) {

            // User is already logged in. Move to main activity
            Intent intent = new Intent(this,
                    LogoutActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_signin:

                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            case R.id.register_button:
                name = etFullName.getText().toString();
                email = etEmailRegister.getText().toString();
                password = etPasswordRegister.getText().toString();
                token = AppController.getString(getBaseContext(), "token");

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(isNetworkAvailable()) {
                        new CreateUser(this, name, email, password, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                if(!output.equals("null")){
                                    //do nothing
                                    Snackbar.make(v, output, Snackbar.LENGTH_LONG).show();

                                } else {
                                    session.setLogin(true);
                                    intent = new Intent(Register.this, CreateProfileActivity.class);
                                    intent.putExtra("new", true);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).execute();
                    }
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }

                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
