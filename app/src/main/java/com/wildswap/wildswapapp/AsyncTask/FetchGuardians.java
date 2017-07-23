package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.SparseArray;

import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

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

/**
 * Created by Chris on 04-Mar-16.
 *
 */
public class FetchGuardians extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ArrayList<String> emails = new ArrayList<>();
    private ProgressDialog pDialog;
    private Context context;
    Boolean showPDialog = true;
    String cid;
    String siteAdmin;

    StoredData inst = new StoredData();
    private SparseArray<User> fetchedUsers = new SparseArray<>();
    User thisUser = inst.getLoggedInUser();


    public FetchGuardians(Context context, String cid, String siteAdmin, Boolean showPDialog) {
        this.context = context;
        this.cid = cid;
        this.siteAdmin = siteAdmin;
        this.showPDialog = showPDialog;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(showPDialog) {
            pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Fetching Guardians...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        // issue the post request
        try {
            String json = getGuardians();
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    int size = jObj.getInt("size");

                    if(size > 0) {

                        JSONObject jsonUser;
                        for (int i = 0; i < size; i++) {
                            User user = new User();
                            jsonUser = jObj.getJSONObject("user" + i);

                            user.setProfile_pic(jsonUser.getString("profile_pic"));
                            user.setName(jsonUser.getString("name"));
                            user.setEmail(jsonUser.getString("email"));
                            user.setUserType(jsonUser.getString("userType"));
                            user.setToken(jsonUser.getString("token"));
                            user.setUid(jsonUser.getString("unique_uid"));

                            fetchedUsers.put(i, user);
                        }

                        inst.setGuardians(fetchedUsers);
                        System.out.println("added to dealers");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once donepDialog.dismiss();
        if(showPDialog) {
            pDialog.dismiss();
        }
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
        } catch (SocketTimeoutException | ConnectException e) {
            e.printStackTrace();
        }

        return "{\"tag\":\"" + "fetchGuardians" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String getGuardians() {

        return "{\"tag\":\"" + "fetchGuardians" + "\","
                + "\"cid\":\"" + cid + "\"}";
    }

}
