package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 04-Mar-16.
 */
public class FetchUnknownSites extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    String user;
    final int relatOwn = 90;
    final int relatTrade = 45;
    List<LatLng> unknownSites = new ArrayList<LatLng>();

    public FetchUnknownSites(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Fetching Unknown Sites ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = AppController.getString(context, "uid");
        // issue the post request
        try {
            String json = getKnownSites(user, relatOwn, relatTrade);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL_REGISTER, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                int size = jObj.getInt("size");

                if(!error) {
                    for (int i = 0; i < size; i++) {
                        JSONObject site = jObj.getJSONObject("site" + i);
                        String longitude = site.getString("longitude");
                        String latitude = site.getString("latitude");
                        double lon = Double.parseDouble(longitude);
                        double lat = Double.parseDouble(latitude);
                        LatLng unknown = new LatLng(lat, lon);

                        unknownSites.add(unknown);
                    }
                    knownSite inst = new knownSite();
                    inst.setUnknownSites(unknownSites);
                    inst.setUnknownSitesSize(size);

                } else {
                    //error message
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

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        System.out.println("request: "+request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String getKnownSites(String uid, int relatOwn, int relatTrade) {
        return "{\"tag\":\"" + "unknownSites" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"relatOwn\":\"" + relatOwn + "\","
                + "\"relatTrade\":\"" + relatTrade + "\"}";
    }

}
