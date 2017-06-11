package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.wildswap.wildswapapp.AppController;
import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

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

public class UpdateToken extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private AsyncResponse delegate = null;

    private ProgressDialog pDialog;
    private Context context;
    String user;
    String postResponse;
    private String email;
    private String token;
    Boolean error;
    private String errorMsg;
    StoredData inst = new StoredData();
    User thisUser = new User();

    public UpdateToken(Context context, String email, String token, AsyncResponse delegate) {
        this.context = context;
        this.email = email;
        this.token = token;
        this.delegate = delegate;
    }


    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Updating Token ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
        //System.out.println(getResponse);

        // issue the post request
        try {
            String json = register(email, token);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {
                JSONObject jObj = new JSONObject(postResponse);
                error = jObj.getBoolean("error");

                if(!error){

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

    private String register(String email, String token) {
        return "{\"tag\":\"" + "updateToken" + "\","
                + "\"email\":\"" + email + "\","
                + "\"token\":\"" + token + "\"}";
    }
}
