package scot.wildcamping.wildswap.AsyncTask;

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
import scot.wildcamping.wildswap.Appconfig;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 16-Mar-16.
 *
 */
public class GiftSite extends AsyncTask<String, String, String> {

    private AsyncResponse delegate = null;

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    private ProgressDialog pDialog;
    private Context context;

    String uid;
    String cid;
    String recipient_uid;
    String postResponse;
    String output;

    public GiftSite(Context context, String recipient_uid, String cid, AsyncResponse delegate) {

        this.context = context;
        this.recipient_uid = recipient_uid;
        this.cid = cid;
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
        pDialog.setMessage("Gifting site ...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        uid = thisUser.getUid();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        // issue the post request
        try {
            String json = giftSite(uid, recipient_uid, cid);
            System.out.println("json: "+json);
            postResponse = doPostRequest(Appconfig.URL, json);
            System.out.println("post response: "+postResponse);
            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");

                if(!error) {
                    int gifted = thisUser.getNumGifted();
                    thisUser.setNumGifted(gifted+1);

                    output = "Site gifted!";

                } else {
                    output = jObj.getString("error_msg");
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

        delegate.processFinish(output);
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

    private String giftSite(String uid, String recipient_uid, String cid) {

        return "{\"tag\":\"" + "giftSite" + "\","
                + "\"uid\":\"" + recipient_uid + "\","
                + "\"cid\":\"" + cid + "\"}";
    }
}
