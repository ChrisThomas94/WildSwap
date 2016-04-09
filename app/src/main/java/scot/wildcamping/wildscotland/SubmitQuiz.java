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
import scot.wildcamping.wildscotland.model.Question;
import scot.wildcamping.wildscotland.model.Quiz;

/**
 * Created by Chris on 09-Apr-16.
 */
public class SubmitQuiz extends AsyncTask<String, String, String>{

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private ProgressDialog pDialogKnownSites;
    private Context context;
    String user;
    String email;
    int answer1;
    int answer2;
    int answer3;
    int answer4;
    int answer5;
    SparseArray<Question> arrayQuestions = new SparseArray<>();

    public SubmitQuiz(Context context, int answer1, int answer2, int answer3, int answer4, int answer5) {
        this.context = context;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
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

        user = AppController.getString(context, "uid");
        email = AppController.getString(context, "email");

        // issue the post request
        try {
            String json = submitAnswers(user);
            System.out.println("json: " + json);

            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {

                JSONObject jObj = new JSONObject(postResponse);
                Boolean error = jObj.getBoolean("error");
                if (!error) {
                    int size = jObj.getInt("size");

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

    private String submitAnswers(String uid) {
        return "{\"tag\":\"" + "answers" + "\","
                + "\"uid\":\"" + uid+ "\","
                + "\"question1\":\"" + answer1 + "\","
                + "\"question2\":\"" + answer2 + "\","
                + "\"question3\":\"" + answer3 + "\","
                + "\"question4\":\"" + answer4 + "\","
                + "\"question5\":\"" + answer5 + "\"}";
    }

}
