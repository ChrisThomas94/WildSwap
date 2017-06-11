package com.wildswap.wildswapapp.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
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
import com.wildswap.wildswapapp.TradeActivitySimple;


public class CreateNotification extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();
    public AsyncResponse delegate = null;


    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    public Context context;
    String user;
    String postResponse;
    private String token;
    private String key;

    public CreateNotification(Context context, String token, AsyncResponse delegate) {
        this.context = context;
        this.key = Appconfig.serverKey;
        this.token = token;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Creating product
     */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();
        System.out.println("token: " + token);

        // issue the post request
        try {
            System.out.println("context " + context);
            String json;
            if (context instanceof TradeActivitySimple) {
                json = tradeNotification(token);
            } else {
                json = tradeResponseNotification(token);
            }

            System.out.println("json: " + json);

            postResponse = doPostRequest(Appconfig.firebase, json);      //json
            System.out.println("post response: " + postResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    protected void onPostExecute(String file_url) {

        System.out.println("Create Notification on Post Execute");

        // dismiss the dialog once done
        try {
            JSONObject resp = new JSONObject(postResponse);

            int error = resp.getInt("failure");


            if (error == 1) {
                JSONArray jsonArr = resp.getJSONArray("results");

                JSONObject errorObj = jsonArr.getJSONObject(0);

                String errorMsg = errorObj.getString("error");

                System.out.println("notification error"+errorMsg);

                if(errorMsg.equals("NotRegistered")){
                }

            } else {
                System.out.println("No error" + resp.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        delegate.processFinish(file_url);

    }

    private String doPostRequest(String url, String json) throws IOException {
        try {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "key=" + key)
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
        return "{\"tag\":\"" + "notification" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String tradeNotification(String token) {
        return "{\"notification\"" + ":{" +
                "\"title\":\"" + "Trade Request" + "\","
                + "\"body\":\"" + "You have received a trade request!"
                + "\"},"
                + "\"to\":\"" + token + "\"}";
    }

    private String tradeResponseNotification(String token) {
        return "{\"notification\"" + ":{" +
                "\"title\":\"" + "Trade Response" + "\","
                + "\"body\":\"" + "You have received a response to your trade request!"
                + "\"},"
                + "\"to\":\"" + token + "\"}";
    }

}
