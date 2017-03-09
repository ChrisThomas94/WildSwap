package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.SparseArray;


import org.json.JSONArray;
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
public class FetchSiteImages extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialogKnownSites;
    private Context context;
    String user;
    String cid;
    String email;
    final int relatOwn = 90;
    final int relatTrade = 45;
    String image;
    SparseArray<Site> map = new SparseArray<>();
    SparseArray<Site> owned = new SparseArray<>();
    SparseArray<Gallery> images = new SparseArray<>();
    SparseArray<Gallery> imagesOwnedSite = new SparseArray<>();
    SparseArray<Gallery> imagesKnownSite = new SparseArray<>();



    public FetchSiteImages(Context context, String cid) {
        this.context = context;
        this.cid = cid;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialogKnownSites = new ProgressDialog(context);
        pDialogKnownSites.setMessage("Fetching Images ...");
        pDialogKnownSites.setIndeterminate(false);
        pDialogKnownSites.setCancelable(true);
        pDialogKnownSites.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        //user = AppController.getString(context, "uid");
        //email = AppController.getString(context, "email");

        // issue the post request
        try {
            String json = getImages(cid);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {

                    JSONArray jsonArr = jObj.getJSONArray("images");
                    JSONObject jsonImages = jsonArr.getJSONObject(0);
                    String image1 = jsonImages.getString("image1");
                    String image2 = jsonImages.getString("image2");
                    String image3 = jsonImages.getString("image3");

                    Gallery images = new Gallery();
                    images.setCid(cid);
                    images.setImage1(image1);
                    images.setImage2(image2);
                    images.setImage3(image3);

                    String id = cid.substring(cid.length()-8);
                    int cidEnd = Integer.parseInt(id);

                    System.out.println("cid End: "+cidEnd);

                    SparseArray<Gallery> gallery = new SparseArray<>();
                    gallery.put(cidEnd, images);

                    knownSite inst = new knownSite();
                    inst.setImages(gallery);

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

    private String getImages(String cid) {
        return "{\"tag\":\"" + "images" + "\","
                + "\"cid\":\"" + cid + "\"}";
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
