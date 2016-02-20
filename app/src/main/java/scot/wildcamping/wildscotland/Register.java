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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Register extends AppCompatActivity implements View.OnClickListener {

    TextView tvLogin;
    TextInputLayout fullName;
    TextInputLayout emailRegister;
    TextInputLayout passwordRegister;
    EditText etFullName;
    EditText etEmailRegister;
    EditText etPasswordRegister;
    Button registerButton;

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

        //setting toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

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

    /*
    function to register user details in mysql database
     */
    private void registerUser(final String name, final String email,
                              final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        Map<String, String> params = new HashMap<>();
        //params.put("tag", "register");
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);

        final JSONObject jsonParams = new JSONObject(params);
        final String requestBody = jsonParams.toString();

        CustomRequest custReq = new CustomRequest(Request.Method.POST, Appconfig.URL_REGISTER, params, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject jObj) {
                // do these if it request was successful
                hideDialog();

                try {
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        //writing the value to sharedpreference which we will be showing in main screen
                        AppController.setString(Register.this, "uid", uid);
                        AppController.setString(Register.this, "name", name);
                        AppController.setString(Register.this, "email", email);
                        AppController.setString(Register.this, "created_at", created_at);

                        System.out.println(jObj);

                        // Launch login activity
                        Intent intent = new Intent(
                                Register.this,
                                LogOut.class);
                        startActivity(intent);
                        finish();
                    } else {
                        System.out.println(jObj);
                        // Error occurred in registration. Get the error
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data "+e.toString());
                    Log.e("log_tag", "Failed data was:\n");
                    Log.e("Volley","Error");

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // do these if it request has errors
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonParams, "utf-8");
                    return null;
                }
            }
        };




        System.out.println("custReq: "+custReq);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(custReq, tag_string_req);
        //Volley.newRequestQueue(this).add(custReq);
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
                Intent intent = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(intent);
                finish();
            case R.id.register_button:
                String name = etFullName.getText().toString();
                String email = etEmailRegister.getText().toString();
                String password = etPasswordRegister.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}
