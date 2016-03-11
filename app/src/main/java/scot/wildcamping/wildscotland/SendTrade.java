package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Chris on 11-Mar-16.
 */
public class SendTrade extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    String user;
    final int tradeStatus = 0;
    SparseArray<Site> tradeItems = new SparseArray<>();
    LatLng sendSite;
    LatLng recieveSite;

    public SendTrade(Context context, Site sendSite, Site recieveSite) {
        this.context = context;
        tradeItems.put(0, sendSite);
        tradeItems.put(1, recieveSite);
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

        user = AppController.getString(context, "uid");
        sendSite = tradeItems.get(0).getPosition();
        recieveSite = tradeItems.get(1).getPosition();

        // issue the post request
        try {
            String json = tradeRequest(user, tradeStatus, sendSite.latitude, sendSite.longitude, recieveSite.latitude, recieveSite.longitude);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL_REGISTER, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                int size = jObj.getInt("size");

                if(!error) {
                    for (int i = 0; i < size; i++) {
                        /*JSONObject jsonSite = jObj.getJSONObject("site" + i);
                        String longitude = jsonSite.getString("longitude");
                        String latitude = jsonSite.getString("latitude");
                        double lon = Double.parseDouble(longitude);
                        double lat = Double.parseDouble(latitude);
                        LatLng unknown = new LatLng(lat, lon);

                        Site siteClass = new Site();
                        siteClass.setPosition(unknown);
                        siteClass.setTitle(jsonSite.getString("title"));
                        siteClass.setDescription(jsonSite.getString("description"));
                        siteClass.setRating(jsonSite.getDouble("rating"));
                        siteClass.setFeature1(jsonSite.getString("feature1"));
                        siteClass.setFeature2(jsonSite.getString("feature2"));
                        siteClass.setFeature3(jsonSite.getString("feature3"));
                        siteClass.setFeature4(jsonSite.getString("feature4"));
                        siteClass.setFeature5(jsonSite.getString("feature5"));
                        siteClass.setFeature6(jsonSite.getString("feature6"));
                        siteClass.setFeature7(jsonSite.getString("feature7"));
                        siteClass.setFeature8(jsonSite.getString("feature8"));
                        siteClass.setFeature9(jsonSite.getString("feature9"));
                        siteClass.setFeature10(jsonSite.getString("feature10"));*/
                    }
                    knownSite inst = new knownSite();
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

    private String tradeRequest(String uid, int tradeStatus, Double sendLat, Double sendLon, Double recieveLat, Double recieveLon) {
        return "{\"tag\":\"" + "tradeRequest" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"tradeStatus\":\"" + tradeStatus + "\","
                + "\"sendLat\":\"" + sendLat + "\","
                + "\"sendLon\":\"" + sendLon + "\","
                + "\"recieveLat\":\"" + recieveLat + "\","
                + "\"recieveLon\":\"" + recieveLon + "\"}";
    }
}
