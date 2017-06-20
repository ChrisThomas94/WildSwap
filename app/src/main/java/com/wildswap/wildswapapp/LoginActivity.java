package com.wildswap.wildswapapp;
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

import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchTradeRequests;
import com.wildswap.wildswapapp.AsyncTask.Login;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{    //***

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    Button registerHere;
    Button signIn;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;
    String email;
    String password;
    Intent intent;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing views
        registerHere=(Button)findViewById(R.id.registerhere_button);
        signIn=(Button)findViewById(R.id.signin_button);
        emailLogin=(TextInputLayout)findViewById(R.id.email_loginlayout);
        passwordLogin=(TextInputLayout)findViewById(R.id.password_loginlayout);
        etEmailLogin = (EditText) findViewById(R.id.email_login);
        etPasswordLogin = (EditText) findViewById(R.id.password_login);

        //setting onclick listeners
        registerHere.setOnClickListener(this);
        signIn.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());

        if(thisUser.getEmail() == null){
            session.setLogin(false);
        }

    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //on clicking register button move to Register Activity
            case R.id.registerhere_button:
                intent = new Intent(getApplicationContext(),
                        Register.class);
                startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.signin_button:

                email = etEmailLogin.getText().toString();
                password = etPasswordLogin.getText().toString();

                if(isNetworkAvailable()) {
                    // Check for empty data, commented out until API is fixed
                    if (email.trim().length() > 0 && password.trim().length() > 0) {

                        // Launching  main activity
                        intent = new Intent(LoginActivity.this,
                                MainActivity_Spinner.class);

                        // login user
                        new Login(this, email, password, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {

                                if(output != null){
                                    new FetchTradeRequests(LoginActivity.this, true, new AsyncResponse() {
                                        @Override
                                        public void processFinish(String output) {
                                            session.setLogin(true);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).execute();

                                }
                            }
                        }).execute();

                    } else {
                        // show snackbar to enter credentials
                        Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_LONG).show();
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