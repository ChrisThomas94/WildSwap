package scot.wildcamping.wildswap.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildswap.Appconfig;
import scot.wildcamping.wildswap.Objects.Gallery;
import scot.wildcamping.wildswap.Objects.Site;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 16-Mar-16.
 *
 */
public class CreateSite extends AsyncTask<String, String, String> {

    private AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    private ProgressDialog pDialog;
    private Context context;
    private int relat;
    private String lat;
    private String lon;
    private String title;
    private String description;
    private String classification;
    private String rating;
    private String json;

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

    Boolean permission;
    String distant;
    String nearby;
    String immediate;
    String tag = "addSite";
    String email;
    Double latUpperBound;
    Double latLowerBound;
    Double lonUpperBound;
    Double lonLowerBound;

    String uid;
    String cid;
    String postResponse;
    SparseArray<Site> ownedSites = new SparseArray<>();
    int ownedSitesSize;

    ArrayList<String> images;
    ArrayList<String> imagesSingleLine;


    public CreateSite(Context context, int relationship, String lat, String lon, String title, String description, String classification,
                      String rating,Boolean permission, String distant, String nearby, String immediate,
                      Boolean feature1, Boolean feature2, Boolean feature3, Boolean feature4, Boolean feature5, Boolean feature6, Boolean feature7, Boolean feature8, Boolean feature9, Boolean feature10,
                      ArrayList<String> images, AsyncResponse delegate) {

        this.context = context;
        this.relat = relationship;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.description = description;
        this.classification = classification;
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

        this.permission = permission;
        this.distant = distant;
        this.nearby = nearby;
        this.immediate = immediate;

        Double latDouble = Double.parseDouble(lat);
        Double lonDouble = Double.parseDouble(lon);

        this.latLowerBound = latDouble-0.001;
        this.latUpperBound = latDouble+0.001;
        this.lonLowerBound = lonDouble-0.001;
        this.lonUpperBound = lonDouble+0.001;

        this.images = images;

        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Adding site ...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        imagesSingleLine = new ArrayList<>();
        if(images.size() != 0) {
            for(int i = 0; i< images.size(); i++){

                Uri imageUri = Uri.parse(images.get(i));

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    String im = getStringImage(bitmap);
                    imagesSingleLine.add(i,im.replaceAll("[\r\n]+", ""));

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        uid = thisUser.getUid();
        email = thisUser.getEmail();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        // issue the post request
        try {
            String json = addSite(uid, email, relat, lat, lon, title, description, classification, rating, permission, distant, nearby, immediate, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, latLowerBound, latUpperBound, lonLowerBound, lonUpperBound, imagesSingleLine);
            System.out.println("json: "+json);
            postResponse = doPostRequest(Appconfig.URL, json);
            System.out.println("post response: "+postResponse);
            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");

                if(!error) {
                    Site newSite = new Site();

                    newSite.setCid(jObj.getString("cid"));
                    cid = jObj.get("cid").toString();
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
                    newSite.setClassification(jsonSite.getString("classification"));

                    newSite.setPermission(jsonSite.getString("permission"));
                    newSite.setDistant(jsonSite.getString("distantTerrain"));
                    newSite.setNearby(jsonSite.getString("nearbyTerrain"));
                    newSite.setImmediate(jsonSite.getString("immediateTerrain"));

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

                    newSite.setSiteAdmin(jsonSite.getString("site_admin"));

                    System.out.println("owned site size" + ownedSitesSize);

                    //instance of known site class
                    StoredData inst = new StoredData();
                    //get current owned sites
                    ownedSites = inst.getOwnedSitesMap();
                    ownedSitesSize = inst.getOwnedSiteSize();
                    //add the new site into the stored collection
                    ownedSites.put(ownedSitesSize, newSite);
                    //set the new collection
                    inst.setOwnedSitesMap(ownedSites);

                    //get current known images
                    SparseArray<Gallery> knownGallery = inst.getImages();
                    //Create instance of Gallery class and set images
                    Gallery gallery = new Gallery();
                    gallery.setGallery(imagesSingleLine);
                    gallery.setCid(cid);

                    if(!imagesSingleLine.isEmpty()){
                        gallery.setHasGallery(true);
                    } else {
                        gallery.setHasGallery(false);
                    }

                    //create int id from cid
                    String id = cid.substring(cid.length()-8);
                    int cidEnd = Integer.parseInt(id);
                    //add the new gallery into the storred collection
                    knownGallery.put(cidEnd, gallery);
                    //set the new collection
                    inst.setImages(knownGallery);


                }
            } catch (JSONException e){
                e.printStackTrace();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return cid;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once done
        pDialog.dismiss();


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
            e.printStackTrace();
        }

        delegate.processFinish(file_url);
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String addSite(String uid, String email, int relat, String lat, String lon, String title, String description, String classification, String rating, Boolean permission, String distant, String nearby, String immediate, Boolean feature1, Boolean feature2, Boolean feature3, Boolean feature4, Boolean feature5, Boolean feature6, Boolean feature7, Boolean feature8, Boolean feature9, Boolean feature10, Double latLowerBound, Double latUpperBound, Double lonLowerBound, Double lonUpperBound, ArrayList<String> images) {

        JSONArray jsonGallery = new JSONArray();

        for(int i = 0; i<images.size(); i++){

            JSONObject jsonSingleImage = new JSONObject();

            try {
                jsonSingleImage.put("image" + i, images.get(i));
            }catch(JSONException j){
                j.printStackTrace();
            }

            jsonGallery.put(jsonSingleImage);

        }

        String withImage = "{\"tag\":\"" + tag + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"email\":\"" + email + "\","
                + "\"relat\":\"" + relat + "\","
                + "\"lat\":\"" + lat + "\","
                + "\"lon\":\"" + lon + "\","
                + "\"title\":\"" + title + "\","
                + "\"description\":\"" + description + "\","
                + "\"classification\":\"" + classification + "\","
                + "\"rating\":\"" + rating + "\","
                + "\"permission\":\"" + permission + "\","
                + "\"distant\":\"" + distant + "\","
                + "\"nearby\":\"" + nearby + "\","
                + "\"immediate\":\"" + immediate + "\","
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
                + "\"latLowerBound\":\"" + latLowerBound + "\","
                + "\"latUpperBound\":\"" + latUpperBound + "\","
                + "\"lonLowerBound\":\"" + lonLowerBound + "\","
                + "\"lonUpperBound\":\"" + lonUpperBound + "\","
                + "\"images\":" + jsonGallery + "}";

        String withoutImage = "{\"tag\":\"" + tag + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"email\":\"" + email + "\","
                + "\"relat\":\"" + relat + "\","
                + "\"lat\":\"" + lat + "\","
                + "\"lon\":\"" + lon + "\","
                + "\"title\":\"" + title + "\","
                + "\"description\":\"" + description + "\","
                + "\"classification\":\"" + classification + "\","
                + "\"rating\":\"" + rating + "\","
                + "\"permission\":\"" + permission + "\","
                + "\"distant\":\"" + distant + "\","
                + "\"nearby\":\"" + nearby + "\","
                + "\"immediate\":\"" + immediate + "\","
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
                + "\"latLowerBound\":\"" + latLowerBound + "\","
                + "\"latUpperBound\":\"" + latUpperBound + "\","
                + "\"lonLowerBound\":\"" + lonLowerBound + "\","
                + "\"lonUpperBound\":\"" + lonUpperBound + "\"}";

        if(images.isEmpty()){
            return withoutImage;
        } else {
            return withImage;
        }
    }

    private String getStringImage(Bitmap bmp){
        if(bmp != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            return null;
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
