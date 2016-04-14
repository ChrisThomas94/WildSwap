package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chris on 14-Apr-16.
 */
public class UpdateProfile extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;

    String postResponse;

    private Context context;
    String user;
    String name;
    String email;
    String bio;

    public UpdateProfile(Context context, String name, String email, String bio) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.bio = bio;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(context);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = AppController.getString(context, "uid");

        try {
            String json = updateKnownSite(user, name, email, bio);
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
        // dismiss the dialog once done
        pDialog.dismiss();

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {

                Toast.makeText(context, "Profile Updated!", Toast.LENGTH_LONG).show();

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


    private String updateKnownSite(String uid, String name, String email, String bio){
        return "{\"tag\":\"" + "updateProfile" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"name\":\"" + name + "\","
                + "\"email\":\"" + email + "\","
                + "\"bio\":\"" + bio + "\"}";
    }
}
