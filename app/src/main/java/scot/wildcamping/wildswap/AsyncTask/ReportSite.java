package scot.wildcamping.wildswap.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildswap.Appconfig;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 04-May-17.
 *
 */

public class ReportSite extends AsyncTask<String, String, String> {

    private AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialog;
    Context context;
    String cid;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();


    public ReportSite(Context context, String cid, AsyncResponse delegate){
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
        pDialog.setMessage("Reporting site ...");
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
            json = reportSite(cid);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");

                if (!error) {
                    thisUser.setNumReported(thisUser.getNumReported()+1);

                } else {

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
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once done
        delegate.processFinish(file_url);

        Log.d("Report Site", "Post Execute");
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

    private String reportSite(String cid) {
        return "{\"tag\":\"" + "report" + "\","
                + "\"uid\":\"" + thisUser.getUid() + "\","
                + "\"cid\":\"" + cid + "\"}";
    }


}
