package scot.wildcamping.wildscotland;
import android.app.ProgressDialog;
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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import scot.wildcamping.wildscotland.AsyncTask.AsyncResponse;
import scot.wildcamping.wildscotland.AsyncTask.FetchLogin;

public class Login extends AppCompatActivity implements View.OnClickListener{    //***

    Button registerHere;
    Button signIn;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;
    String email;
    String password;
    Boolean error;
    Intent intent;

    private ProgressDialog progressDialog;
    private SessionManager session;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

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

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        session = new SessionManager(getApplicationContext());


        //If the session is logged in move to MainActivity
        if (session.isLoggedIn()) {

            /*if(isNetworkAvailable()){
                try {
                    String known_results = new FetchKnownSites(this).execute().get();
                    String unknown_result = new FetchUnknownSites(this).execute().get();
                    String trade_result = new FetchTradeRequests(this).execute().get();
                } catch (ExecutionException e){

                } catch (InterruptedException e){

                }
            }*/

            Intent intent = new Intent(Login.this, MainActivity_Spinner.class);
            startActivity(intent);
            finish();
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
                        intent = new Intent(Login.this,
                                MainActivity_Spinner.class);

                        // login user
                        new FetchLogin(this, email, password, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {

                                if(output !=null){
                                    session.setLogin(true);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).execute();

                    } else {
                        // show snackbar to enter credentials
                        Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                                .show();
                    }
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