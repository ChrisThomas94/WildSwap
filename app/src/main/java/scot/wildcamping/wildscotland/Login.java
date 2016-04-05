package scot.wildcamping.wildscotland;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity implements View.OnClickListener{    //***

    Button registerHere;
    Button signIn;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;
    String email;
    String password;
    String errorMsg;
    Boolean error;

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

    class checkLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Logging in ...");
            showDialog();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
            //System.out.println(getResponse);

            // issue the post request
            try {
                String json = login(email, password);
                System.out.println("json: " + json);
                String postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);
                try{
                    JSONObject jObj = new JSONObject(postResponse);
                    error = jObj.getBoolean("error");

                    if(!error) {
                        String userId= jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");

                        AppController.setString(Login.this, "uid", userId);
                        AppController.setString(Login.this, "name", name);
                        AppController.setString(Login.this, "email", email);
                        if (userId != null) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            // Launching  main activity
                            Intent intent = new Intent(Login.this,
                                    MainActivity_Spinner.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // login error
                        errorMsg = jObj.getString("error_msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            progressDialog.dismiss();

            if (error) {
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        }

        private String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);

            System.out.println("body: " + body.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: "+request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        private String login(String email, String password) {
            return "{\"tag\":\"" + "login" + "\","
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + password + "\"}";
        }
    }

    /*
    function to show dialog
     */
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /*
    function to hide dialog
     */
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //on clicking register button move to Register Activity
            case R.id.registerhere_button:
                Intent intent = new Intent(getApplicationContext(),
                        Register.class);
                startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.signin_button:

                email = etEmailLogin.getText().toString();
                password = etPasswordLogin.getText().toString();

                // Check for empty data, commented out until API is fixed
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    new checkLogin().execute();
                } else {
                    // show snackbar to enter credentials
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