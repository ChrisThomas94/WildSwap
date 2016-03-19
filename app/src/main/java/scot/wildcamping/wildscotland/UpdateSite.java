package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chris on 15-Mar-16.
 */
public class UpdateSite extends AsyncTask<String, String, String> {


    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;

    String postResponse;

    private Context context;
    Boolean active = true;
    String cid;
    String title;
    String description;
    Double rating;
    String feature1;
    String feature2;
    String feature3;
    String feature4;
    String feature5;
    String feature6;
    String feature7;
    String feature8;
    String feature9;
    String feature10;

    public UpdateSite(Context context, Boolean active, String cid, String title, String description, Double rating, String feature1, String feature2, String feature3, String feature4, String feature5, String feature6, String feature7, String feature8, String feature9, String feature10) {
        this.context = context;
        this.active = active;
        this.cid = cid;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.feature3 = feature3;
        this.feature4 = feature4;
        this.feature5 = feature5;
        this.feature6 = feature6;
        this.feature7 = feature7;
        this.feature8 = feature8;
        this.feature9 = feature9;
        this.feature10 = feature10;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);

        if(!active){
            pDialog.setMessage("Deleting site...");
        } else {
            pDialog.setMessage("Updating site...");
        }

        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        if(!active){
            try {
                String json = deleteSite(cid, active);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL_REGISTER, json);      //json
                System.out.println("post response: " + postResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // issue the post request
            try {
                String json = updateSite(cid, active);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL_REGISTER, json);      //json
                System.out.println("post response: " + postResponse);

                try {

                    JSONObject jObj = new JSONObject(postResponse);
                    Boolean error = jObj.getBoolean("error");
                    //int size = jObj.getInt("size");
                    if (!error) {
                        //for (int i = 0; i < size; i++) {
                        /*JSONObject jsonSite = jObj.getJSONObject("site" + i);
                        String longitude = jsonSite.getString("longitude");
                        String latitude = jsonSite.getString("latitude");
                        double lon = Double.parseDouble(longitude);
                        double lat = Double.parseDouble(latitude);
                        LatLng position = new LatLng(lat, lon);

                        Site siteClass = new Site();
                        siteClass.setPosition(position);
                        siteClass.setCid(jsonSite.getString("unique_cid"));
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

                        //map.put(i, siteClass);
                        // }

                        //knownSite inst = new knownSite();
                        //inst.setKnownSitesMap(map);
                        //inst.setKnownSiteSize(size);

                    } else {
                        //error message
                    }

                } catch (JSONException e) {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once done
        pDialog.dismiss();

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error && !active) {

                Toast.makeText(context, "Site Deleted!", Toast.LENGTH_LONG).show();

            } else if(!error && active) {

                Toast.makeText(context, "Site Updated!", Toast.LENGTH_LONG).show();

            } else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){

        }
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

    private String updateSite(String cid, Boolean active) {
        return "{\"tag\":\"" + "updateSite" + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"active\":\"" + active + "\"}";
    }

    private String deleteSite(String cid, Boolean active) {
        return "{\"tag\":\"" + "deleteSite" + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"active\":\"" + active + "\"}";
    }

}
