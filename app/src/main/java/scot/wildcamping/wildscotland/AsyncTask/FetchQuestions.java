package scot.wildcamping.wildscotland.AsyncTask;

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
import scot.wildcamping.wildscotland.AppController;
import scot.wildcamping.wildscotland.Appconfig;
import scot.wildcamping.wildscotland.Objects.Question;
import scot.wildcamping.wildscotland.Objects.Quiz;
import scot.wildcamping.wildscotland.Objects.User;

/**
 * Created by Chris on 09-Apr-16.
 */
public class FetchQuestions extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialogKnownSites;
    private Context context;
    String user;
    String email;
    SparseArray<Question> arrayQuestions = new SparseArray<>();

    public FetchQuestions(Context context, String email) {
        this.context = context;
        this.email = email;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialogKnownSites = new ProgressDialog(context);
        pDialogKnownSites.setMessage("Fetching Questions ...");
        pDialogKnownSites.setIndeterminate(false);
        pDialogKnownSites.setCancelable(true);
        pDialogKnownSites.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        if(email == null){
            user = AppController.getString(context, "user");
        }

        // issue the post request
        try {
            String json = getQuestions(user);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    int size = jObj.getInt("size");

                    //AppController.setString(context, "user_name", jObj.getString("name"));
                    //AppController.setString(context, "user_email", jObj.getString("email"));
                    //AppController.setString(context, "user_bio", jObj.getString("bio"));
                    //AppController.setString(context, "user_profile_pic", jObj.getString("profile_pic"));

                    JSONObject jsonQuestion;
                    Question question;
                    for (int i = 0; i < size; i++) {
                        jsonQuestion = jObj.getJSONObject("question" + i);
                        question = new Question();

                        question.setQuestion(jsonQuestion.getString("question"));
                        question.setAnswer1(jsonQuestion.getString("answer1"));
                        question.setAnswer2(jsonQuestion.getString("answer2"));
                        question.setAnswer3(jsonQuestion.getString("answer3"));
                        question.setAnswer4(jsonQuestion.getString("answer4"));
                        question.setAnswer(jsonQuestion.getInt("answer"));

                        arrayQuestions.put(i, question);
                    }

                    Quiz inst = new Quiz();
                    inst.setQuestions(arrayQuestions);

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

    private String getQuestions(String uid) {
        return "{\"tag\":\"" + "questions" + "\","
                + "\"email\":\"" + email+ "\"}";
    }

}
