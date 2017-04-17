package scot.wildcamping.wildscotland.AsyncTask;

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
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.AppController;
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.Objects.StoredData;

/**
 * Created by Chris on 04-Mar-16.
 *
 */
public class FetchUsers extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();
    private AsyncResponse delegate = null;

    private ArrayList<String> emails = new ArrayList<>();
    private ProgressDialog pDialog;
    private Context context;
    private Boolean boolAllUsers = true;

    StoredData inst = new StoredData();
    private SparseArray<User> fetchedUsers = new SparseArray<>();
    User thisUser = inst.getLoggedInUser();


    public FetchUsers(Context context, ArrayList<String> emails, AsyncResponse delegate) {
        this.context = context;
        this.emails = emails;
        this.delegate = delegate;

        if(!emails.equals(null)){
            boolAllUsers = false;
        }

    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching User Data...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        // issue the post request
        try {
            String json = getAllUsers();
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    int size = jObj.getInt("size");

                    JSONObject jsonUser;
                    for (int i = 0; i < size; i++) {
                        User user = new User();
                        jsonUser = jObj.getJSONObject("user" + i);

                        if(boolAllUsers){

                            user.setProfile_pic(jsonUser.getString("profile_pic"));
                            user.setName(jsonUser.getString("name"));
                            user.setEmail(jsonUser.getString("email"));

                        } else {

                            user.setProfile_pic(jsonUser.getString("profile_pic"));
                            user.setCover_pic(jsonUser.getString("cover_pic"));
                            user.setName(jsonUser.getString("name"));
                            user.setEmail(jsonUser.getString("email"));
                            user.setBio(jsonUser.getString("bio"));
                            user.setWhy(jsonUser.getString("why"));
                            user.setUserType(jsonUser.getString("userType"));
                            user.setToken(jsonUser.getString("token"));
                            user.setUid(jsonUser.getString("unique_uid"));
                            user.setNumTrades(jsonUser.getInt("numTrades"));
                            user.setNumSites(jsonUser.getInt("numSites"));

                        }

                        fetchedUsers.put(i, user);
                    }

                    if(fetchedUsers.get(0).getEmail().equals(thisUser.getEmail())){
                        inst.setLoggedInUser(fetchedUsers.get(0));
                        System.out.println("added to logged in user");
                    } else {
                        inst.setDealers(fetchedUsers);
                        System.out.println("added to dealers");

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
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
        delegate.processFinish(file_url);
        pDialog.dismiss();
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

    private String getAllUsers() {
        JSONArray users = new JSONArray(emails);

        return "{\"tag\":\"" + "allUsers" + "\","
                + "\"boolAllUsers\":\"" + boolAllUsers+ "\","
                + "\"someUsers\":" + users + "}";
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
