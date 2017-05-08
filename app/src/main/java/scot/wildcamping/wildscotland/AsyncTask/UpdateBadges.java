package scot.wildcamping.wildscotland.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.StoredData;
import scot.wildcamping.wildscotland.Objects.User;

/**
 * Created by Chris on 14-Apr-16.
 *
 */
public class UpdateBadges extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    public AsyncResponse delegate = null;

    private ProgressDialog pDialog;

    String postResponse;

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    private Context context;
    String user;
    Boolean update;

    public UpdateBadges(Context context, Boolean update, AsyncResponse delegate) {
        this.context = context;
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
        pDialog.setMessage("Updating Badges...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();
        ArrayList<Integer> badges = thisUser.getBadges();

        try {
            String json = updateBadges(user, badges);
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
        delegate.processFinish(file_url);


        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {


                if(update){
                    Toast.makeText(context, "Badges Updated!", Toast.LENGTH_LONG).show();

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


    private String updateBadges(String uid, ArrayList<Integer> badges){

        JSONArray jsonBadges = new JSONArray(badges);

        return "{\"tag\":\"" + "updateBadges" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"badges\":\"" + jsonBadges + "\"}";
    }
}
