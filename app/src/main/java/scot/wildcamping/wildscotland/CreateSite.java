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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.model.Site;

/**
 * Created by Chris on 16-Mar-16.
 */
public class CreateSite extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    int relat;
    String lat;
    String lon;
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
    String tag = "addSite";

    String uid;
    String postResponse;
    SparseArray<Site> ownedSites = new SparseArray<>();
    int ownedSitesSize;

    public CreateSite(Context context, int relationship, String lat, String lon, String title, String description, String rating, Boolean feature1, Boolean feature2, Boolean feature3, Boolean feature4, Boolean feature5, Boolean feature6, Boolean feature7, Boolean feature8, Boolean feature9, Boolean feature10, String imageMultiLine) {

        this.context = context;
        this.relat = relationship;
        this.lat = lat;
        this.lon = lon;
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
        String imageSingleLine = imageMultiLine.replaceAll("[\r\n]+", "");
        this.image = imageSingleLine;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Adding site ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        uid = AppController.getString(context, "uid");
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
        //System.out.println(getResponse);

        // issue the post request
        try {
            String json = addSite(uid, relat, lat, lon, title, description, rating, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, image);
            System.out.println("json: "+json);
            System.out.println("image 64base: " + image);
            postResponse = doPostRequest(Appconfig.URL_REGISTER, json);
            System.out.println("create site post response: "+postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");

                if(!error) {
                    Site newSite = new Site();

                    newSite.setCid(jObj.getString("cid"));
                    JSONObject jsonSite = jObj.getJSONObject("site");
                    newSite.setSiteAdmin(jsonSite.getString("site_admin"));

                    String longitude = jsonSite.getString("lon");
                    String latitude = jsonSite.getString("lat");
                    double lon = Double.parseDouble(longitude);
                    double lat = Double.parseDouble(latitude);
                    LatLng position = new LatLng(lat, lon);

                    newSite.setPosition(position);
                    newSite.setTitle(jsonSite.getString("title"));
                    newSite.setDescription(jsonSite.getString("description"));
                    newSite.setRating(jsonSite.getDouble("rating"));
                    newSite.setFeature1(jsonSite.getString("feature1"));
                    newSite.setFeature2(jsonSite.getString("feature2"));
                    newSite.setFeature3(jsonSite.getString("feature3"));
                    newSite.setFeature4(jsonSite.getString("feature4"));
                    newSite.setFeature5(jsonSite.getString("feature5"));
                    newSite.setFeature6(jsonSite.getString("feature6"));
                    newSite.setFeature7(jsonSite.getString("feature7"));
                    newSite.setFeature8(jsonSite.getString("feature8"));
                    newSite.setFeature9(jsonSite.getString("feature9"));
                    newSite.setFeature10(jsonSite.getString("feature10"));
                    newSite.setImage(jsonSite.getString("image"));

                    //knownSite inst = new knownSite();
                    //ownedSites = inst.getOwnedSitesMap();
                    //ownedSitesSize = inst.getOwnedSiteSize();
                    //ownedSites.put(ownedSitesSize, newSite);
                    //inst.setOwnedSitesMap(ownedSites);
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
        //pDialog.dismiss();
        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {

                Toast.makeText(context, "Site added!", Toast.LENGTH_LONG).show();

            }else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){

        }
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        //RequestBody body = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("image", "filename", RequestBody.create(MediaType.parse("image/jpeg"), new File(target.getPath()))).build();

        System.out.println("body: " + body.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        System.out.println("request: "+request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String addSite(String uid, int relat, String lat, String lon, String title, String description, String rating, Boolean feature1, Boolean feature2, Boolean feature3, Boolean feature4, Boolean feature5, Boolean feature6, Boolean feature7, Boolean feature8, Boolean feature9, Boolean feature10, String image) {
        return "{\"tag\":\"" + tag + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"relat\":\"" + relat + "\","
                + "\"lat\":\"" + lat + "\","
                + "\"lon\":\"" + lon + "\","
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

}
