package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.Objects.Site;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 04-Mar-16.
 *
 */
public class FetchUnknownSites extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();
    private AsyncResponse delegate = null;

    ProgressDialog pDialog;
    private Context context;
    String user;
    final int relatOwn = 90;
    final int relatTrade = 45;
    private SparseArray<Site> unknownSites = new SparseArray<>();
    Geocoder geocoder;
    Boolean showDialog = true;

    StoredData inst = new StoredData();
    SparseArray<Site> knownSites = new SparseArray<>();
    User thisUser = inst.getLoggedInUser();
    int knownSize;

    public FetchUnknownSites(Context context, Boolean showDialog, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        this.showDialog = showDialog;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        geocoder = new Geocoder(context, Locale.getDefault());

        if(showDialog) {
            pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Fetching Unknown Sites ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        knownSize = inst.getKnownSiteSize();
        knownSites = inst.getKnownSitesMap();
        user = thisUser.getUid();

        // issue the post request
        try {
            String json = getUnknownSites(user, relatOwn, relatTrade);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            JSONObject jObj = new JSONObject(postResponse);
            Boolean error = jObj.getBoolean("error");
            int size = jObj.getInt("size");
            System.out.println("unknown sites size: " + size);

            if(!error) {
                int counter = 0;
                for (int i = 0; i < size; i++) {
                    Site siteClass = new Site();
                    JSONObject jsonSite = jObj.getJSONObject("site" + i);
                    JSONObject jsonDetails = jsonSite.getJSONObject("details");
                    System.out.println("json site: "+jsonSite);

                    boolean knownError = false;


                    for(int j = 0; j< knownSize; j++){

                        if (knownSites.get(j).getCid().equals(jsonDetails.getString("unique_cid"))){
                            System.out.print("This is actually a known site: " + jsonDetails.getString("unique_cid"));
                            knownError = true;

                        }
                    }

                    if(!knownError){
                        //int populairty = jsonSite.getInt("pop");
                        siteClass.setPopularity(jsonSite.getInt("pop"));

                        String longitude = jsonDetails.getString("longitude");
                        String latitude = jsonDetails.getString("latitude");
                        double lon = Double.parseDouble(longitude);
                        double lat = Double.parseDouble(latitude);
                        LatLng unknown = new LatLng(lat, lon);

                        siteClass.setCid(jsonDetails.getString("unique_cid"));
                        siteClass.setPosition(unknown);
                        siteClass.setTitle(jsonDetails.getString("title"));
                        siteClass.setDescription(jsonDetails.getString("description"));
                        siteClass.setClassification(jsonDetails.getString("classification"));
                        siteClass.setSuited(jsonDetails.getString("suited"));

                        Double d = Double.parseDouble(jsonDetails.getString("rating"));

                        siteClass.setRating(d);
                        siteClass.setFeature1(jsonDetails.getString("feature1"));
                        siteClass.setFeature2(jsonDetails.getString("feature2"));
                        siteClass.setFeature3(jsonDetails.getString("feature3"));
                        siteClass.setFeature4(jsonDetails.getString("feature4"));
                        siteClass.setFeature5(jsonDetails.getString("feature5"));
                        siteClass.setFeature6(jsonDetails.getString("feature6"));
                        siteClass.setFeature7(jsonDetails.getString("feature7"));
                        siteClass.setFeature8(jsonDetails.getString("feature8"));
                        siteClass.setFeature9(jsonDetails.getString("feature9"));
                        siteClass.setFeature10(jsonDetails.getString("feature10"));
                        siteClass.setSiteAdmin(jsonDetails.getString("site_admin"));
                        siteClass.setToken(jsonSite.getString("token"));

                        unknownSites.put(counter, siteClass);
                        counter++;
                    } else {
                        System.out.println("known sites error");
                    }
                }

                inst.setUnknownSitesMap(unknownSites);
                inst.setUnknownSitesSize(unknownSites.size());
            }

        } catch (JSONException e){
            e.printStackTrace();
        } catch (IOException e){
        e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        if(showDialog) {
            pDialog.dismiss();
        }
        delegate.processFinish(file_url);
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
        return "{\"tag\":\"" + "unknownSites" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String getUnknownSites(String uid, int relatOwn, int relatTrade) {
        return "{\"tag\":\"" + "unknownSites" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"relatOwn\":\"" + relatOwn + "\","
                + "\"relatTrade\":\"" + relatTrade + "\"}";
    }

}
