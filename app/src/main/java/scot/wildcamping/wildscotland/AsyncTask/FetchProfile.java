package scot.wildcamping.wildscotland.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.AppController;
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.StoredData;
import scot.wildcamping.wildscotland.Objects.StoredUsers;
import scot.wildcamping.wildscotland.Objects.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chris on 23-Mar-17.
 *
 */

public class FetchProfile extends AsyncTask<String, String, String> {

    public AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    ProgressDialog pDialog;
    private Context context;
    String user;
    String email;
    String password;
    Boolean error = true;
    String errorMsg;
    String userId;
    StoredData inst = new StoredData();
    User thisUser = new User();

    public FetchProfile(Context context, String email, String password, AsyncResponse delegate) {
        this.context = context;
        this.email = email;
        this.password = password;
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
        pDialog.setMessage("Logging in ...");
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
            String json = login(email, password);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);
            try{
                JSONObject jObj = new JSONObject(postResponse);
                error = jObj.getBoolean("error");

                if(!error) {
                    userId= jObj.getString("uid");
                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("name");
                    String email = user.getString("email");
                    String bio = user.getString("bio");
                    String why = user.getString("why");
                    String userType = user.getString("userType");
                    String profile_pic = user.getString("profile_pic");
                    String cover_pic = user.getString("cover_pic");
                    String token = user.getString("token");


                    thisUser.setUid(userId);
                    thisUser.setName(name);
                    thisUser.setEmail(email);
                    thisUser.setBio(bio);
                    thisUser.setWhy(why);
                    thisUser.setUserType(userType);
                    thisUser.setCover_pic(cover_pic);
                    thisUser.setProfile_pic(profile_pic);
                    thisUser.setToken(token);

                    inst.setLoggedInUser(thisUser);

                } else {
                    // login error
                    errorMsg = jObj.getString("error_msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
        delegate.processFinish(userId);

        pDialog.dismiss();

        if (error) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
        }
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        System.out.println("body: " + body.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        System.out.println("request: "+request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String login(String email, String password) {
        return "{\"tag\":\"" + "login" + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\"}";

    }
}
