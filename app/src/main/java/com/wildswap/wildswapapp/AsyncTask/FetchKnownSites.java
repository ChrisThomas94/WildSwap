package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.List;
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
public class FetchKnownSites extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();
    private AsyncResponse delegate = null;

    ProgressDialog pDialog;
    Context context;
    String user;
    String email;
    final int relatOwn = 90;
    final int relatTrade = 45;
    StoredData inst = new StoredData();

    SparseArray<Site> map = new SparseArray<>();
    SparseArray<Site> owned = new SparseArray<>();
    User thisUser = inst.getLoggedInUser();
    Geocoder geocoder;
    Boolean showDialog = true;

    public FetchKnownSites(Context context, Boolean showDialog, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        this.showDialog = showDialog;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        geocoder = new Geocoder(context, Locale.getDefault());

        if(showDialog) {
            pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Fetching Sites ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();
        email = thisUser.getEmail();

        // issue the post request
        try {
            String json = getKnownSites(user, relatOwn);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    int size = jObj.getInt("size");
                    int ownedCnt = 0;
                    int knownCnt = 0;

                    JSONObject jsonSite;
                    Site siteClass;
                    for (int i = 0; i < size; i++) {
                        jsonSite = jObj.getJSONObject("site" + i);
                        System.out.println("json site: "+jsonSite);

                        String cid = jsonSite.getString("unique_cid");
                        String admin = jsonSite.getString("site_admin");
                        String latitude = jsonSite.getString("latitude");
                        String longitude = jsonSite.getString("longitude");
                        String title = jsonSite.getString("title");
                        String description = jsonSite.getString("description");
                        String classification = jsonSite.getString("classification");
                        String rating = jsonSite.getString("rating");
                        String permission = jsonSite.getString("permission");
                        String distant = jsonSite.getString("distantTerrain");
                        String nearby = jsonSite.getString("nearbyTerrain");
                        String immediate = jsonSite.getString("immediateTerrain");
                        int numOwners = jsonSite.getInt("num_owners");

                        //features

                        String display = jsonSite.getString("display_pic");

                        Double avrRating = jsonSite.getDouble("avr_rating");
                        int ratedBy = jsonSite.getInt("no_of_raters");

                        double lon = Double.parseDouble(longitude);
                        double lat = Double.parseDouble(latitude);
                        LatLng position = new LatLng(lat, lon);

                        List<android.location.Address> address = geocoder.getFromLocation(lat, lon, 1);

                        siteClass = new Site();
                        siteClass.setPosition(position);
                        siteClass.setCid(cid);
                        siteClass.setSiteAdmin(admin);
                        siteClass.setLat(lat);
                        siteClass.setLon(lon);
                        siteClass.setAddress(address);
                        siteClass.setTitle(title);
                        siteClass.setDescription(description);
                        siteClass.setRating(avrRating);
                        siteClass.setRatedBy(ratedBy);
                        siteClass.setClassification(classification);
                        siteClass.setPermission(permission);
                        siteClass.setDistant(distant);
                        siteClass.setNearby(nearby);
                        siteClass.setImmediate(immediate);
                        siteClass.setNumOwners(numOwners);
                        siteClass.setFeature1(jsonSite.getString("feature1"));
                        siteClass.setFeature2(jsonSite.getString("feature2"));
                        siteClass.setFeature3(jsonSite.getString("feature3"));
                        siteClass.setFeature4(jsonSite.getString("feature4"));
                        siteClass.setFeature5(jsonSite.getString("feature5"));
                        siteClass.setFeature6(jsonSite.getString("feature6"));
                        siteClass.setFeature7(jsonSite.getString("feature7"));
                        siteClass.setFeature8(jsonSite.getString("feature8"));
                        siteClass.setFeature9(jsonSite.getString("feature9"));
                        siteClass.setFeature10(jsonSite.getString("feature10"));
                        siteClass.setDisplay_pic(display);

                        if (admin.equals(email)) {
                            System.out.println("owned site FOUND!");
                            owned.put(ownedCnt, siteClass);
                            ownedCnt++;
                        } else {
                            map.put(knownCnt, siteClass);
                            knownCnt++;
                        }
                    }

                    inst.setKnownSitesMap(map);
                    inst.setOwnedSitesMap(owned);
                    inst.setKnownSiteSize(knownCnt);
                    inst.setOwnedSiteSize(ownedCnt);

                } else {
                    //error message
                    String errMsg = jObj.getString("error_msg");
                    Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
                }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException j){
            j.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(String file_url) {

        if(showDialog) {
            if ((pDialog != null) && pDialog.isShowing()) {
                pDialog.dismiss();
            }
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
            System.out.println("request: " + request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (ConnectException e) {
            e.printStackTrace();

        } catch (NoRouteToHostException e) {
            e.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();
        }

        return "{\"tag\":\"" + "knownSites" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String getKnownSites(String uid, int relat) {
        return "{\"tag\":\"" + "knownSites" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"relat\":\"" + relat + "\"}";
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
