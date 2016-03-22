package scot.wildcamping.wildscotland;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.model.StoredTrades;
import scot.wildcamping.wildscotland.model.Trade;

/**
 * Created by Chris on 12-Mar-16.
 */
public class FetchTradeRequests extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialogTrades;
    private Context context;
    String user;
    final int tradeStatus = 0;
    SparseArray<Trade> activeTrades = new SparseArray<>();

    SparseArray<Trade> sentTrades = new SparseArray<>();
    SparseArray<Trade> receivedTrades = new SparseArray<>();

    public FetchTradeRequests(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialogTrades = new ProgressDialog(context);
        pDialogTrades.setMessage("Fetching Trade Requests...");
        pDialogTrades.setIndeterminate(false);
        pDialogTrades.setCancelable(true);
        pDialogTrades.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = AppController.getString(context, "uid");
        // issue the post request
        try {
            String json = getTradeRequests(user, tradeStatus);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);



            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                int size = jObj.getInt("size");

                if(!error) {
                    for (int i = 0; i < size; i++) {

                        JSONObject jsonTrade = jObj.getJSONObject("trade" + i);

                        Trade activeTrade = new Trade();
                        activeTrade.setUnique_tid(jsonTrade.getString("unique_tid"));
                        activeTrade.setSender_uid(jsonTrade.getString("sender_uid_fk"));
                        activeTrade.setReciever_uid(jsonTrade.getString("reciever_uid_fk"));
                        activeTrade.setSend_cid(jsonTrade.getString("send_cid_fk"));
                        activeTrade.setRecieve_cid(jsonTrade.getString("recieve_cid_fk"));
                        activeTrade.setDate(jsonTrade.getString("created_at"));

                        if(jsonTrade.getString("sender_uid_fk").equals(user)){
                            activeTrade.setUserRelation("Sent");
                        } else {
                            activeTrade.setUserRelation("Received");
                        }

                        activeTrades.put(i, activeTrade);
                    }
                    StoredTrades inst = new StoredTrades();
                    inst.setActiveTrades(activeTrades);
                    inst.setActiveTradesSize(size);

                } else {
                    //error message
                }

            } catch (JSONException e){

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
        pDialogTrades.dismiss();
    }

    private String doGetRequest(String url)throws IOException{
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
        System.out.println("request: "+request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String getTradeRequests(String uid, int tradeStatus) {
        return "{\"tag\":\"" + "activeTrades" + "\","
                + "\"uid\":\"" + uid + "\","
                + "\"tradeStatus\":\"" + tradeStatus+ "\"}";
    }

}
