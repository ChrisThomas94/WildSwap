package scot.wildcamping.wildswap.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

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
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 15-Mar-16.
 *
 */
public class UpdateSite extends AsyncTask<String, String, String> {

    private AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    String postResponse;

    private Context context;
    Boolean owned;
    Boolean active = true;
    String cid;
    String title;
    String description;
    String user;
    String classification;
    String rating;

    ArrayList<String> images;
    ArrayList<String> imagesSingleLine;

    public UpdateSite(Context context, Boolean owned , Boolean active, String cid, String title, String description, String classification, String rating, ArrayList<String> images, AsyncResponse delegate) {
        this.context = context;
        this.owned = owned;
        this.active = active;
        this.cid = cid;
        this.title = title;
        this.description = description;
        this.images = images;
        this.classification = classification;
        this.rating = rating;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        user = thisUser.getUid();

        if(active){
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Updating site...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

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
                String json = updateSite(active, user, cid, title, description, classification, imagesSingleLine);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (active){
            try {
                String json = updateKnownSite(active, user, cid, rating);
                System.out.println("json: " + json);
                postResponse = doPostRequest(Appconfig.URL, json);      //json
                System.out.println("post response: " + postResponse);

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

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error && !active) {

                Toast.makeText(context, "Site Deleted!", Toast.LENGTH_LONG).show();

            } else if(!error && active) {
                pDialog.dismiss();

                Toast.makeText(context, "Site Updated!", Toast.LENGTH_LONG).show();

            } else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){

        }

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

    private String updateSite(boolean active, String user, String cid, String title, String description, String classification, ArrayList<String> images) {

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

        String withImage =  "{\"tag\":\"" + "updateSite" + "\","
                + "\"active\":\"" + active + "\","
                + "\"uid\":\"" + user + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"title\":\"" + title + "\","
                + "\"description\":\"" + description + "\","
                + "\"classification\":\"" + classification + "\","
                + "\"images\":" + jsonGallery + "}";

        String withoutImage = "{\"tag\":\"" + "updateSite" + "\","
                + "\"active\":\"" + active + "\","
                + "\"uid\":\"" + user + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"title\":\"" + title + "\","
                + "\"description\":\"" + description + "\","
                + "\"classification\":\"" + classification + "\"}";

        if(images.isEmpty()){
            return withoutImage;
        } else {
            return withImage;
        }


    }

    private String deleteSite(String cid, Boolean active) {
        return "{\"tag\":\"" + "deleteSite" + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"active\":\"" + active + "\"}";
    }

    private String updateKnownSite(Boolean active, String uid, String cid, String rating){
        return "{\"tag\":\"" + "updateKnownSite" + "\","
                + "\"active\":\"" + active + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"cid\":\"" + cid + "\","
                + "\"rating\":\"" + rating + "\"}";
    }

    private String getStringImage(Bitmap bmp){
        if(bmp != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            return null;
        }
    }

}
