package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 14-Apr-16.
 *
 */
public class UpdateBadges extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    public AsyncResponse delegate = null;

    private ProgressDialog pDialog;

    String postResponse;

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    private Context context;
    String user;
    Boolean update;

    public UpdateBadges(Context context, Boolean update, AsyncResponse delegate) {
        this.context = context;
        this.update = update;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    protected void onPreExecute() {
        //super.onPreExecute();

        /*pDialog = new ProgressDialog(context);
        pDialog.setMessage("Updating Badges...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();*/

    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();
        ArrayList<Integer> badges = thisUser.getBadges();

        try {
            String json = updateBadges(user, badges);
            System.out.println("json: " + json);
            postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {
                JSONObject resp = new JSONObject(postResponse);

                boolean error = resp.getBoolean("error");
                if (!error) {

                    JSONObject jsonBadges = resp.getJSONObject("badges");

                    ArrayList<Integer> newBadges = new ArrayList<>();

                    System.out.println("badges length "+jsonBadges.length());

                    for(int i = 1; i<=jsonBadges.length()-4;i++){
                        int badge = jsonBadges.getInt("badge_"+i);
                        System.out.println("badge "+ badge);
                        newBadges.add(i-1, badge);
                    }

                    thisUser.setBadges(newBadges);

                } else {
                    String errMsg = resp.getString("error_msg");
                    Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e){

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(final String file_url) {

        System.out.println("on post execute");
        delegate.processFinish("win");

    }

    private String doPostRequest(String url, String json) throws IOException {
        try{
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: " + request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (ConnectException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "{\"tag\":\"" + "updateBadges" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }


    private String updateBadges(String uid, ArrayList<Integer> badges){

        JSONArray jsonBadges = new JSONArray(badges);

        return "{\"tag\":\"" + "updateBadges" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"badges\":\"" + jsonBadges + "\"}";
    }
}
