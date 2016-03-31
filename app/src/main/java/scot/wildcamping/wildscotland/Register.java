package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Register extends AppCompatActivity implements View.OnClickListener {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    TextView tvLogin;
    TextInputLayout fullName;
    TextInputLayout emailRegister;
    TextInputLayout passwordRegister;
    EditText etFullName;
    EditText etEmailRegister;
    EditText etPasswordRegister;
    Button registerButton;
    String name;
    String email;
    String password;
    Intent intent;

    SessionManager session;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing Views
        registerButton = (Button) findViewById(R.id.register_button);
        fullName = (TextInputLayout) findViewById(R.id.fullname_registerlayout);
        emailRegister = (TextInputLayout) findViewById(R.id.email_registerlayout);
        passwordRegister = (TextInputLayout) findViewById(R.id.password_registerlayout);
        etFullName = (EditText) findViewById(R.id.fullname_register);
        etEmailRegister = (EditText) findViewById(R.id.email_register);
        etPasswordRegister = (EditText) findViewById(R.id.password_register);
        tvLogin = (TextView) findViewById(R.id.tv_signin);


        tvLogin.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in
        if (session.isLoggedIn()) {
            // User is already logged in. Move to main activity
            Intent intent = new Intent(Register.this,
                    LogOut.class);
            startActivity(intent);
            finish();
        }

    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            name = etFullName.getText().toString();
            email = etEmailRegister.getText().toString();
            password = etPasswordRegister.getText().toString();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
            //System.out.println(getResponse);

            // issue the post request
            try {
                String json = register(name, email, password);
                System.out.println("json: " + json);
                String postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);

                session.setLogin(true);
                try {
                    JSONObject jObj = new JSONObject(postResponse);
                    Boolean error = jObj.getBoolean("error");

                    if(!error){
                    String userId = jObj.getString("uid");

                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("name");
                    String email = user.getString("email");
                    AppController.setString(Register.this, "uid", userId);
                    AppController.setString(Register.this, "name", name);
                    AppController.setString(Register.this, "email", email);

                    intent = new Intent(Register.this,
                            MainActivity_Spinner.class);
                    startActivity(intent);
                    finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(Register.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e){

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
            pDialog.dismiss();

        }

        private String doGetRequest(String url)throws IOException{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
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

        private String register(String name, String email, String password) {
            return "{\"tag\":\"" + "register" + "\","
                    + "\"name\":\"" + name + "\","
                    + "\"email\":\"" + email + "\","
                    + "\"password\":\"" + password + "\"}";
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin:
                intent = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(intent);
                finish();
            case R.id.register_button:
                name = etFullName.getText().toString();
                email = etEmailRegister.getText().toString();
                password = etPasswordRegister.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    new CreateNewProduct().execute();
                        //registerUser(name, email, password);
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}
