package scot.wildcamping.wildswap.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildswap.Appconfig;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 11-Mar-16.
 *
 */
public class CreateTrade extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    String user;
    final int tradeStatus = 0;
    String postResponse;

    private String send_cid;
    private String recieve_cid;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    public CreateTrade(Context context, String send_cid, String recieve_cid) {
        this.context = context;
        this.send_cid = send_cid;
        this.recieve_cid = recieve_cid;
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
            String json = tradeRequest(user, tradeStatus, send_cid, recieve_cid);
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
        // dismiss the dialog once done
        pDialog.dismiss();

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {

                Toast.makeText(context, "Trade Sent!", Toast.LENGTH_LONG).show();

            }else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        System.out.println("request: "+request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String tradeRequest(String uid, int tradeStatus, String send_cid, String recieve_cid) {
        return "{\"tag\":\"" + "tradeRequest" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"tradeStatus\":\"" + tradeStatus + "\","
                + "\"send_cid\":\"" + send_cid + "\","
                + "\"recieve_cid\":\"" + recieve_cid + "\"}";
    }
}