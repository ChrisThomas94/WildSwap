package scot.wildcamping.wildscotland.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.StoredData;
import scot.wildcamping.wildscotland.Objects.StoredTrades;
import scot.wildcamping.wildscotland.Objects.Trade;
import scot.wildcamping.wildscotland.Objects.User;

/**
 * Created by Chris on 12-Mar-16.
 *
 */
public class FetchTradeRequests extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    Context context;
    private AsyncResponse delegate = null;
    private ProgressDialog pDialog;
    String user;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    private SparseArray<Trade> activeTrades = new SparseArray<>();
    private SparseArray<Trade> inactiveTrades = new SparseArray<>();
    private SparseArray<Trade> acceptedTrades = new SparseArray<>();
    private SparseArray<Trade> rejectedTrades = new SparseArray<>();


    private SparseArray<Trade> sentTrades = new SparseArray<>();
    private SparseArray<Trade> receivedTrades = new SparseArray<>();


    public FetchTradeRequests(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        Log.d("Fetch Trades", "Fetch Trades Pre Execute");
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching Trades ...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();
        // issue the post request
        try {
            String json = getTradeRequests(user);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                int size = jObj.getInt("size");
                int sentCnt = 0;
                int receivedCnt = 0;
                int openCnt = 0;
                int closedCnt = 0;
                int acceptedCnt = 0;
                int rejectedCnt = 0;

                if(!error) {
                    for (int i = 0; i < size; i++) {

                        JSONObject jsonTrade = jObj.getJSONObject("trade" + i);

                        Trade trade = new Trade();
                        trade.setUnique_tid(jsonTrade.getString("unique_tid"));
                        trade.setSender_uid(jsonTrade.getString("sender_uid_fk"));
                        trade.setReciever_uid(jsonTrade.getString("reciever_uid_fk"));
                        trade.setSend_cid(jsonTrade.getString("send_cid_fk"));
                        trade.setRecieve_cid(jsonTrade.getString("recieve_cid_fk"));
                        trade.setDate(jsonTrade.getString("created_at"));
                        trade.setStatus(jsonTrade.getInt("status"));

                        if(jsonTrade.getString("sender_uid_fk").equals(user)){
                            trade.setUserRelation("Sent");
                            sentTrades.put(sentCnt, trade);
                            sentCnt++;
                        } else {
                            trade.setUserRelation("Received");
                            receivedTrades.put(receivedCnt, trade);
                            receivedCnt++;
                        }

                        if(jsonTrade.getInt("status") == 0){
                            activeTrades.put(openCnt, trade);
                            openCnt++;
                        } else if (jsonTrade.getInt("status") == 1 || jsonTrade.getInt("status") == 2) {

                            if(jsonTrade.getInt("status") == 1){
                                acceptedTrades.put(acceptedCnt, trade);
                                acceptedCnt++;

                            } else if(jsonTrade.getInt("status") == 2){
                                rejectedTrades.put(rejectedCnt, trade);
                                rejectedCnt++;
                            }

                            inactiveTrades.put(closedCnt, trade);
                            closedCnt++;
                        }
                    }

                    StoredTrades inst = new StoredTrades();
                    inst.setActiveTrades(activeTrades);
                    inst.setActiveTradesSize(activeTrades.size());

                    inst.setInactiveTrades(inactiveTrades);
                    inst.setInactiveTradesSize(inactiveTrades.size());

                    inst.setSentTrades(sentTrades);
                    inst.setReceivedTrades(receivedTrades);

                    inst.setAcceptedTradesSize(acceptedTrades.size());
                    inst.setAcceptedTrades(acceptedTrades);

                    inst.setRejectedTradesSize(rejectedTrades.size());
                    inst.setRejectedTrades(rejectedTrades);

                }

            } catch (JSONException e){
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

        delegate.processFinish(file_url);

        Log.d("Fetch Images", "Post Execute");
        pDialog.dismiss();

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

    private String getTradeRequests(String uid) {
        return "{\"tag\":\"" + "allTrades" + "\","
                + "\"uid\":\"" + uid+ "\"}";
    }

}
