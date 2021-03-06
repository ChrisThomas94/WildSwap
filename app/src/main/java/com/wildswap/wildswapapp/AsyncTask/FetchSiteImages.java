package com.wildswap.wildswapapp.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.wildswap.wildswapapp.Appconfig;
import com.wildswap.wildswapapp.Objects.Gallery;
import com.wildswap.wildswapapp.Objects.StoredData;

/**
 *
 * Created by Chris on 04-Mar-16.
 *
 */
public class FetchSiteImages extends AsyncTask<String, String, String> {

    private AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    private Context context;
    String user;
    private String cid;
    public Gallery gallery;



    public FetchSiteImages(Context context, String cid, AsyncResponse delegate) {
        this.context = context;
        this.cid = cid;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching Images ...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        // issue the post request
        String json = null;
        try {
            json = getImages(cid);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                System.out.println("post response: " + postResponse);

                //create an instance of Known sites
                StoredData inst = new StoredData();
                //Create instance of Gallery class
                gallery = new Gallery();
                //get current known images
                SparseArray<Gallery> knownGallery = inst.getImages();

                if (!error) {

                    JSONArray jsonArr = jObj.getJSONArray("images");
                    ArrayList<String> images = new ArrayList<>();


                    for(int i=0; i<jsonArr.length();i++){
                        System.out.println("image array item" + i + " " + jsonArr.get(i));
                        JSONObject jsonImages = jsonArr.getJSONObject(i);
                        String image = jsonImages.getString("image1");
                        images.add(i, image);
                    }

                    //set images
                    gallery.setGallery(images);
                    gallery.setCid(cid);
                    gallery.setHasGallery(true);
                    //create int id from cid
                    String id = cid.substring(cid.length()-8);
                    int cidEnd = Integer.parseInt(id);
                    //add the new gallery into the storred collection
                    knownGallery.put(cidEnd, gallery);
                    //set the new collection
                    inst.setImages(knownGallery);

                } else {
                    //error message
                    gallery.setCid(cid);
                    gallery.setHasGallery(false);

                    String id = cid.substring(cid.length()-8);
                    int cidEnd = Integer.parseInt(id);
                    //add the new gallery into the storred collection
                    knownGallery.put(cidEnd, gallery);
                    inst.setImages(knownGallery);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(String result) {

        System.out.println("on post execute result"+ result);

        delegate.processFinish(result);

        pDialog.dismiss();

    }


    private String doPostRequest(String url, String json) throws IOException {

        try {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: " + request);
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (ConnectException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "{\"tag\":\"" + "images" + "\","
                + "\"error\":\"" + true + "\","
                + "\"error_msg\":\"" + "Server Timeout" + "\"}";
    }

    private String getImages(String cid) {
        return "{\"tag\":\"" + "images" + "\","
                + "\"cid\":\"" + cid + "\"}";
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
