package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.model.Gallery;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

/**
 * Created by Chris on 04-Mar-16.
 */
public class FetchKnownSites extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialogKnownSites;
    private Context context;
    String user;
    String email;
    final int relatOwn = 90;
    final int relatTrade = 45;
    String image;
    SparseArray<Site> map = new SparseArray<>();
    SparseArray<Site> owned = new SparseArray<>();
    SparseArray<Gallery> images = new SparseArray<>();
    SparseArray<Gallery> imagesOwnedSite = new SparseArray<>();
    SparseArray<Gallery> imagesKnownSite = new SparseArray<>();



    public FetchKnownSites(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialogKnownSites = new ProgressDialog(context);
        pDialogKnownSites.setMessage("Fetching Your Sites ...");
        pDialogKnownSites.setIndeterminate(false);
        pDialogKnownSites.setCancelable(true);
        pDialogKnownSites.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = AppController.getString(context, "uid");
        email = AppController.getString(context, "email");

        // issue the post request
        try {
            String json = getKnownSites(user, relatOwn);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

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
                        String longitude = jsonSite.getString("longitude");
                        String latitude = jsonSite.getString("latitude");
                        double lon = Double.parseDouble(longitude);
                        double lat = Double.parseDouble(latitude);
                        LatLng position = new LatLng(lat, lon);

                        siteClass = new Site();
                        siteClass.setPosition(position);
                        siteClass.setCid(jsonSite.getString("unique_cid"));
                        siteClass.setSiteAdmin(jsonSite.getString("site_admin"));
                        siteClass.setLat(lat);
                        siteClass.setLon(lon);
                        siteClass.setTitle(jsonSite.getString("title"));
                        siteClass.setDescription(jsonSite.getString("description"));
                        siteClass.setRating(jsonSite.getDouble("avr_rating"));
                        siteClass.setRatedBy(jsonSite.getInt("no_of_raters"));

                        siteClass.setPermission(jsonSite.getString("permission"));
                        siteClass.setDistant(jsonSite.getString("distantTerrain"));
                        siteClass.setNearby(jsonSite.getString("nearbyTerrain"));
                        siteClass.setImmediate(jsonSite.getString("immediateTerrain"));

                        /*SparseArray<String> features = new SparseArray<>();

                        for(int j = 1; j<11; j++){
                            features.append(j, jsonSite.getString("feature"+j));
                        }
                        siteClass.setFeatures(features);*/

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
                        siteClass.setSiteAdmin(jsonSite.getString("site_admin"));

                        if (jsonSite.getString("site_admin").equals(email)) {
                            System.out.println("owned site FOUND!");
                            owned.put(ownedCnt, siteClass);
                            ownedCnt++;
                        } else {
                            map.put(knownCnt, siteClass);
                            knownCnt++;
                        }
                    }

                    knownSite inst = new knownSite();
                    inst.setKnownSitesMap(map);
                    inst.setOwnedSitesMap(owned);
                    inst.setKnownSiteSize(knownCnt);
                    inst.setOwnedSiteSize(ownedCnt);

                } else {
                    //error message
                }

            } catch (JSONException e) {

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
        try {
            if ((this.pDialogKnownSites != null) && this.pDialogKnownSites.isShowing()) {
                this.pDialogKnownSites.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            this.pDialogKnownSites = null;
        }
    }

    private String doGetRequest(String url) throws IOException {
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
        System.out.println("request: " + request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String getKnownSites(String uid, int relat) {
        return "{\"tag\":\"" + "knownSites" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"relat\":\"" + relat + "\"}";
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
