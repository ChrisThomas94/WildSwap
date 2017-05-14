package scot.wildcamping.wildswap.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;
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
import scot.wildcamping.wildswap.Appconfig;
import scot.wildcamping.wildswap.Objects.Question;
import scot.wildcamping.wildswap.Objects.Quiz;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 09-Apr-16.
 *
 */
public class SubmitQuiz extends AsyncTask<String, String, String>{

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();
    public AsyncResponse delegate = null;

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    private ProgressDialog pDialogKnownSites;
    private Context context;
    String user;
    String email;
    ArrayList<Integer> answers;
    String postResponse;

    public SubmitQuiz(Context context, ArrayList<Integer> answers, AsyncResponse delegate) {
        this.context = context;
        this.answers = answers;
        this.delegate = delegate;

    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialogKnownSites = new ProgressDialog(context);
        pDialogKnownSites.setMessage("Submitting Answers ...");
        pDialogKnownSites.setIndeterminate(false);
        pDialogKnownSites.setCancelable(true);
        pDialogKnownSites.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        user = thisUser.getUid();
        email = thisUser.getEmail();

        // issue the post request
        try {
            String json = submitAnswers(user);
            System.out.println("json: " + json);

            postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    ArrayList<Integer> answers = new ArrayList<>(thisUser.getAnswers());
                    JSONArray jsonAnswers = jObj.getJSONArray("answers");

                    System.out.println("json array:" + jsonAnswers);

                    if(jsonAnswers != null){
                        for(int i = 0; i<jsonAnswers.length(); i++){
                            answers.add(jsonAnswers.getInt(i));
                        }
                    }

                    thisUser.setAnswers(answers);

                } else {
                    //error message
                }

            } catch (JSONException e) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog and add markers
     **/
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once donepDialog.dismiss();

        delegate.processFinish(file_url);

        try {
            if ((this.pDialogKnownSites != null) && this.pDialogKnownSites.isShowing()) {
                this.pDialogKnownSites.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            this.pDialogKnownSites = null;
        }

        try {
            JSONObject resp = new JSONObject(postResponse);

            boolean error = resp.getBoolean("error");
            if (!error) {

                Toast.makeText(context, "Answers Submitted!", Toast.LENGTH_LONG).show();

            } else {
                String errMsg = resp.getString("error_msg");
                Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e){

        }

    }

    private String doGetRequest(String url) throws IOException {
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
        System.out.println("request: " + request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String submitAnswers(String uid) {

        JSONArray answersJSON = new JSONArray(answers);

        return "{\"tag\":\"" + "answers" + "\","
                + "\"uid\":\"" + uid+ "\","
                + "\"answers\":" + answersJSON + "}";
    }

}
