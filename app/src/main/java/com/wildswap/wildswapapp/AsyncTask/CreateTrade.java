package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 11-Mar-16.
 *
 */
public class CreateTrade extends AsyncTask<String, String, String> {

    private AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    String user;
    final int tradeStatus = 0;
    String postResponse;
    String errMsg;

    private String send_cid;
    private String recieve_cid;
    private String tradeLocation;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    public CreateTrade(Context context, String send_cid, String recieve_cid, String tradeLocation, AsyncResponse delegate) {
        this.context = context;
        this.send_cid = send_cid;
        this.recieve_cid = recieve_cid;
        this.tradeLocation = tradeLocation;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Sending Trade Request...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();

        // issue the post request
        try {
            String json = tradeRequest(user, tradeStatus, tradeLocation, send_cid, recieve_cid);
            System.out.println("json: " + json);
            postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        String msg = null;

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {

                msg = "Trade Sent!";
            }else {
                msg = resp.getString("error_msg");
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        delegate.processFinish(msg);

        // dismiss the dialog once done
        pDialog.dismiss();
    }

    private String doPostRequest(String url, String json) throws IOException {
        try{
            RequestBody body = RequestBody.create(JSON, json);

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
        return "{\"tag\":\"" + "tradeRequest" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String tradeRequest(String uid, int tradeStatus, String tradeLocation, String send_cid, String recieve_cid) {
        return "{\"tag\":\"" + "tradeRequest" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"tradeStatus\":\"" + tradeStatus + "\","
                + "\"tradeLocation\":\"" + tradeLocation + "\","
                + "\"send_cid\":\"" + send_cid + "\","
                + "\"recieve_cid\":\"" + recieve_cid + "\"}";
    }
}
