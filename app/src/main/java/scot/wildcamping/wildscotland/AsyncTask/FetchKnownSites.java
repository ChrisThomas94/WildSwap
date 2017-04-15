package scot.wildcamping.wildscotland.AsyncTask;

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
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.AppController;
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.StoredData;

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
    SparseArray<Site> map = new SparseArray<>();
    SparseArray<Site> owned = new SparseArray<>();
    Geocoder geocoder;


    public FetchKnownSites(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        geocoder = new Geocoder(context, Locale.getDefault());

        Log.d("Fetch Known Sites", "Fetch Known Sites Pre Execute");
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching Sites ...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
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
            System.out.println("hello world");

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                System.out.println("error" + error);
                if (!error) {
                    int size = jObj.getInt("size");
                    int ownedCnt = 0;
                    int knownCnt = 0;

                    System.out.println("known sites size: "+size);

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

                    StoredData inst = new StoredData();
                    inst.setKnownSitesMap(map);
                    inst.setOwnedSitesMap(owned);
                    inst.setKnownSiteSize(knownCnt);
                    inst.setOwnedSiteSize(ownedCnt);

                } else {
                    //error message
                    String errMsg = jObj.getString("error_msg");
                    Toast.makeText(context, "Site added!", Toast.LENGTH_LONG).show();
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
        pDialog.dismiss();
        delegate.processFinish(file_url);

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
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
