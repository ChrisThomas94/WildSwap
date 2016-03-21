package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

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
    SparseArray<Site> unknownSites = new SparseArray<>();

    knownSite inst = new knownSite();
    SparseArray<Site> knownSites = new SparseArray<>();
    int knownSize;

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

        knownSize = inst.getKnownSiteSize();

        knownSites = inst.getKnownSitesMap();

        user = AppController.getString(context, "uid");
        // issue the post request
        try {
            String json = getUnknownSites(user, relatOwn, relatTrade);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                int size = jObj.getInt("size");

                if(!error) {
                    int counter = 0;
                    for (int i = 0; i < size; i++) {
                        JSONObject jsonSite = jObj.getJSONObject("site" + i);

                        boolean knownError = false;


                        for(int j = 0; j< knownSize; j++){
                            if (knownSites.get(j).getCid().equals(jsonSite.getString("unique_cid"))){
                                System.out.print("This is actually a known site: " + jsonSite.getString("unique_cid"));
                                knownError = true;
                                size--;

                            }
                        }

                        if(!knownError){
                            String longitude = jsonSite.getString("longitude");
                            String latitude = jsonSite.getString("latitude");
                            double lon = Double.parseDouble(longitude);
                            double lat = Double.parseDouble(latitude);
                            LatLng unknown = new LatLng(lat, lon);

                            Site siteClass = new Site();
                            siteClass.setCid(jsonSite.getString("unique_cid"));
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
                            siteClass.setFeature10(jsonSite.getString("feature10"));
                            siteClass.setSiteAdmin(jsonSite.getString("site_admin"));


                            unknownSites.put(counter, siteClass);
                            counter++;
                        }
                    }

                    inst.setUnknownSitesMap(unknownSites);
                    inst.setUnknownSitesSize(unknownSites.size());

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

    private String getUnknownSites(String uid, int relatOwn, int relatTrade) {
        return "{\"tag\":\"" + "unknownSites" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"relatOwn\":\"" + relatOwn + "\","
                + "\"relatTrade\":\"" + relatTrade + "\"}";
    }

}
