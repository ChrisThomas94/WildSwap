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
    Boolean owned;
    Boolean active = true;
    String uid;
    String cid;
    String title;
    String description;
    String rating;
    Boolean feature1;
    Boolean feature2;
    Boolean feature3;
    Boolean feature4;
    Boolean feature5;
    Boolean feature6;
    Boolean feature7;
    Boolean feature8;
    Boolean feature9;
    Boolean feature10;
    String image;

    public UpdateSite(Context context, Boolean owned , Boolean active, String cid, String title, String description, String rating, Boolean feature1, Boolean feature2, Boolean feature3, Boolean feature4, Boolean feature5, Boolean feature6, Boolean feature7, Boolean feature8, Boolean feature9, Boolean feature10, String image) {
        this.context = context;
        this.owned = owned;
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
        this.image = image;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        uid = AppController.getString(context, "uid");
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

        if(!active && owned){
            try {
                String json = deleteSite(cid, active);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (active && owned) {
            // issue the post request
            try {
                String json = updateSite(active, cid, title, description, rating, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, image);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);

                try {

                    JSONObject jObj = new JSONObject(postResponse);
                    Boolean error = jObj.getBoolean("error");
                    //int size = jObj.getInt("size");
                    if (!error) {


                    } else {
                        //error message
                    }

                } catch (JSONException e) {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (active && !owned){
            try {
                String json = updateKnownSite(active, uid, cid, rating, image);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //none
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

    private String updateSite(boolean active, String cid, String title, String description, String rating, Boolean feature1, Boolean feature2, Boolean feature3, Boolean feature4, Boolean feature5, Boolean feature6, Boolean feature7, Boolean feature8, Boolean feature9, Boolean feature10, String image) {
        return "{\"tag\":\"" + "updateSite" + "\","
                + "\"active\":\"" + active + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"title\":\"" + title + "\","
                + "\"description\":\"" + description + "\","
                + "\"rating\":\"" + rating + "\","
                + "\"feature1\":\"" + feature1 + "\","
                + "\"feature2\":\"" + feature2 + "\","
                + "\"feature3\":\"" + feature3 + "\","
                + "\"feature4\":\"" + feature4 + "\","
                + "\"feature5\":\"" + feature5 + "\","
                + "\"feature6\":\"" + feature6 + "\","
                + "\"feature7\":\"" + feature7 + "\","
                + "\"feature8\":\"" + feature8 + "\","
                + "\"feature9\":\"" + feature9 + "\","
                + "\"feature10\":\"" + feature10 + "\","
                + "\"image\":\"" + image + "\"}";
    }

    private String deleteSite(String cid, Boolean active) {
        return "{\"tag\":\"" + "deleteSite" + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"active\":\"" + active + "\"}";
    }

    private String updateKnownSite(Boolean active, String uid, String cid, String rating, String image){
        return "{\"tag\":\"" + "updateKnownSite" + "\","
                + "\"active\":\"" + active + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"rating\":\"" + rating + "\","
                + "\"image\":\"" + image + "\"}";
    }

}
