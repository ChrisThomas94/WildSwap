package scot.wildcamping.wildscotland.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
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
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.StoredTrades;
import scot.wildcamping.wildscotland.Objects.Trade;

/**
 * Created by Chris on 12-Mar-16.
 *
 */
public class UpdateTrade extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private AsyncResponse delegate = null;

    private ProgressDialog pDialog;
    private Context context;
    private int newTradeStatus;
    int negativeTradeStatus = 1;
    private String unique_tid;
    String postResponse;

    SparseArray<Trade> activeTrades = new SparseArray<>();

    private StoredTrades trades = new StoredTrades();

    private Trade trade = new Trade();


    public UpdateTrade(Context context, String unique_tid, int newTradeStatus, AsyncResponse delegate) {
        this.context = context;
        this.unique_tid = unique_tid;
        this.newTradeStatus = newTradeStatus;
        this.delegate = delegate;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);

        if(newTradeStatus == negativeTradeStatus){
            pDialog.setMessage("Canceling trade...");
        } else {
            pDialog.setMessage("Accepting trade...");
        }

        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        activeTrades = trades.getActiveTrades();
        int size = trades.getActiveTradesSize();

        for(int i=0; i<size; i++){
            //if(activeTrades.get(i).getUnique_tid().equals(unique_tid));
            trade = activeTrades.get(i);
        }

        String sender_uid = trade.getSender_uid();
        String receiver_uid = trade.getReciever_uid();
        String send_cid = trade.getSend_cid();
        String receive_cid = trade.getRecieve_cid();

        // issue the post request
        try {
            String json = getTrade(unique_tid, newTradeStatus, sender_uid, receiver_uid, send_cid, receive_cid);
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

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error && newTradeStatus == 2) {


                Toast.makeText(context, "Trade Accepted!", Toast.LENGTH_LONG).show();

            } else if(!error && newTradeStatus == 1) {

                Toast.makeText(context, "Trade Rejected!", Toast.LENGTH_LONG).show();

            } else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        delegate.processFinish(file_url);
        // dismiss the dialog once done
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

    private String getTrade(String unique_tid, int newTradeStatus, String sender_uid, String receiver_uid, String send_cid, String receive_cid) {

        return "{\"tag\":\"" + "updateTrade" + "\","
                + "\"tid\":\"" + unique_tid + "\","
                + "\"tradeStatus\":\"" + newTradeStatus + "\","
                + "\"sender_uid\":\"" + sender_uid + "\","
                + "\"receiver_uid\":\"" + receiver_uid + "\","
                + "\"send_cid\":\"" + send_cid + "\","
                + "\"receive_cid\":\"" + receive_cid + "\"}";
    }

}
