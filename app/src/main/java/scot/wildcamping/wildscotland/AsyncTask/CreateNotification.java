package scot.wildcamping.wildscotland.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.TradeActivitySimple;
import scot.wildcamping.wildscotland.TradeView_Received;


public class CreateNotification extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    public Context context;
    String user;
    String postResponse;
    private String token;
    private String key;

    public CreateNotification(Context context, String token) {
        this.context = context;
        this.key = Appconfig.serverKey;
        this.token = token;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();

        // issue the post request
        try {
            System.out.println("context " + context);
            String json;
            if(context instanceof TradeActivitySimple){
                json = tradeNotification(token);
            } else {
                json = tradeResponseNotification(token);
            }

            System.out.println("json: " + json);

            postResponse = doPostRequest(Appconfig.firebase, json);      //json
            System.out.println("post response: " + postResponse);

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
        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {
                Log.d("No error", resp.toString());
            }else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){
            Log.d("JSON Exception", e.toString());
        }
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)

                .addHeader("Authorization", "key=" + key)
                .post(body)
                .build();
        System.out.println("request: "+ request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String tradeNotification(String token) {
        return "{\"notification\"" + ":{" +
                "\"title\":\"" + "Trade Request" + "\","
                + "\"body\":\"" + "You have received a trade request!"
                + "\"},"
                + "\"to\":\"" + token + "\"}";
    }

    private String tradeResponseNotification(String token) {
        return "{\"notification\"" + ":{" +
                "\"title\":\"" + "Trade Response" + "\","
                + "\"body\":\"" + "You have received a response to your trade request!"
                + "\"},"
                + "\"to\":\"" + token + "\"}";
    }

}
