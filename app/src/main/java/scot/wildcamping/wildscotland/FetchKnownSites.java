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
import scot.wildcamping.wildscotland.model.Image;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

/**
 * Created by Chris on 04-Mar-16.
 */
public class FetchKnownSites extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    String user;
    final int relatOwn = 90;
    final int relatTrade = 45;
    String image;
    SparseArray<Site> map = new SparseArray<>();
    SparseArray<Site> owned = new SparseArray<>();
    SparseArray<Image> images = new SparseArray<>();
    SparseArray<Image> imagesOwnedSite = new SparseArray<>();
    SparseArray<Image> imagesKnownSite = new SparseArray<>();



    public FetchKnownSites(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Fetching Your Sites ...");
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
            String json = getKnownSites(user, relatOwn);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    int size = jObj.getInt("size");
                    int sizeImages = jObj.getInt("sizeImages");
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
                        siteClass.setFeature10(jsonSite.getString("feature10"));
                        siteClass.setSiteAdmin(jsonSite.getString("site_admin"));

                        for (int j = 0; j < sizeImages; j++) {

                            JSONObject jsonImage = jObj.getJSONObject("image" + j);
                            //Image imageClass = new Image();
                            //imageClass.setImage(jsonImage.getString("image"));
                            //imageClass.setCid(jsonImage.getString("campsite_fk"));

                            //images.put(j, imageClass);

                            if(siteClass.getCid().equals(jsonImage.getString("campsite_fk"))){
                                siteClass.setImage(jsonImage.getString("image"));
                            }
                        }

                        if (jsonSite.getString("site_admin").equals(user)) {
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
        pDialog.dismiss();
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
