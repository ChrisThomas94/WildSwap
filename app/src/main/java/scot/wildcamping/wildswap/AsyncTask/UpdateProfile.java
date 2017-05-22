package scot.wildcamping.wildswap.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildswap.Appconfig;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 14-Apr-16.
 *
 */
public class UpdateProfile extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient.Builder client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS);

    public AsyncResponse delegate = null;

    private ProgressDialog pDialog;

    String postResponse;

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    private Context context;
    String user;
    private String userType;
    private String bio;
    private String why;
    private String profile_pic;
    private String cover_pic;
    Boolean update;

    public UpdateProfile(Context context, String userType, String bio, String why, String profile_pic, String cover_pic, Boolean update, AsyncResponse delegate) {
        this.context = context;
        this.userType = userType;
        this.bio = bio;
        this.why = why;
        this.profile_pic = profile_pic;
        this.cover_pic = cover_pic;
        this.update = update;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Updating profile...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();

        try {
            String json = updateProfile(user, userType, bio, why, profile_pic, cover_pic);
            System.out.println("json: " + json);
            postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(String file_url) {

        String errMsg = "Success";

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {

                thisUser.setUserType(userType);
                thisUser.setBio(bio);
                thisUser.setWhy(why);
                thisUser.setProfile_pic(profile_pic);
                thisUser.setCover_pic(cover_pic);

                if(update){
                    Toast.makeText(context, "Profile Updated!", Toast.LENGTH_LONG).show();

                } else {

                    JSONObject jsonBadges = resp.getJSONObject("badges");

                    ArrayList<Integer> badges = new ArrayList<>();

                    System.out.println("badges length "+jsonBadges.length());

                    for(int i = 1; i<=jsonBadges.length()-4;i++){
                        int badge = jsonBadges.getInt("badge_"+i);
                        System.out.println("badge "+ badge);
                        badges.add(i-1, badge);
                    }

                    thisUser.setBadges(badges);

                    Toast.makeText(context, "Profile Created!", Toast.LENGTH_LONG).show();

                }

            } else {
                errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){

        }

        // dismiss the dialog once done
        pDialog.dismiss();
        delegate.processFinish(errMsg);
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        System.out.println("request: " + request);
        Response response = client.build().newCall(request).execute();
        return response.body().string();
    }


    private String updateProfile(String uid, String userType, String bio, String why, String profile_pic, String cover_pic){
        return "{\"tag\":\"" + "updateProfile" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"userType\":\"" + userType + "\","
                + "\"bio\":\"" + bio + "\","
                + "\"why\":\"" + why + "\","
                + "\"profile_pic\":\"" + profile_pic + "\","
                + "\"cover_pic\":\"" + cover_pic + "\"}";
    }
}
