package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.AppController;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

public class CreateUser extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private AsyncResponse delegate = null;

    private ProgressDialog pDialog;
    private Context context;
    String user;
    String postResponse;
    private String token;
    private String name;
    private String email;
    private String password;
    private String country;
    Boolean error;
    private String errorMsg;
    StoredData inst = new StoredData();
    User thisUser = new User();

    public CreateUser(Context context, String name, String email, String password, String country, AsyncResponse delegate) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.password = password;
        this.country = country;
        this.delegate = delegate;
    }


    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Registering ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        thisUser.setToken(AppController.getString(context, "token"));
        token = thisUser.getToken();
        //AppController.setString(context, "token", "");

    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
        //System.out.println(getResponse);

        // issue the post request
        try {
            String json = register(name, token, email, password, country);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {
                JSONObject jObj = new JSONObject(postResponse);
                error = jObj.getBoolean("error");

                if(!error){
                    String userId = jObj.getString("uid");

                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("name");
                    String email = user.getString("email");
                    String country = user.getString("country");
                    AppController.setString(context, "country", country);
                    AppController.setString(context, "email", email);

                    thisUser.setEmail(email);
                    thisUser.setName(name);
                    thisUser.setUid(userId);
                    thisUser.setCountry(country);
                    ArrayList<Integer> answers = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0));
                    thisUser.setAnswers(answers);
                    inst.setLoggedInUser(thisUser);

                } else {
                    errorMsg = jObj.getString("error_msg");

                }

            } catch (JSONException e){
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
        pDialog.dismiss();
        if (error) {
            delegate.processFinish(errorMsg);
        } else {
            delegate.processFinish("null");

        }
    }

    private String doPostRequest(String url, String json) throws IOException {
        try{
            RequestBody body = RequestBody.create(JSON, json);

            System.out.println("body: " + body.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: "+request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (ConnectException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "{\"tag\":\"" + "register" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String register(String name, String token, String email, String password, String country) {
        return "{\"tag\":\"" + "register" + "\","
                + "\"name\":\"" + name + "\","
                + "\"token\":\"" + token + "\","
                + "\"country\":\"" + country + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\"}";
    }
}
