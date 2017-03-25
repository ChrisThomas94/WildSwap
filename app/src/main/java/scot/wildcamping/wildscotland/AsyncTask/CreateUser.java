package scot.wildcamping.wildscotland.AsyncTask;

import android.app.ProgressDialog;
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
import scot.wildcamping.wildscotland.Objects.StoredUsers;
import scot.wildcamping.wildscotland.Objects.User;

public class CreateUser extends AsyncTask<String, String, String> {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    private AsyncResponse delegate = null;

    private ProgressDialog pDialog;
    private Context context;
    String user;
    String postResponse;
    private String token;
    private String name;
    private String email;
    private String password;
    Boolean error;
    private String errorMsg;

    public CreateUser(Context context, String name, String email, String password, AsyncResponse delegate) {
        this.context = context;
        this.name = name;
        this.email = email;
        this.password = password;
        this.delegate = delegate;
    }


    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Registering ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        token = AppController.getString(context, "token");

    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {

        //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
        //System.out.println(getResponse);

        // issue the post request
        try {
            String json = register(name, token, email, password);
            System.out.println("json: " + json);
            String postResponse = doPostRequest(Appconfig.URL, json);      //json
            System.out.println("post response: " + postResponse);

            try {
                JSONObject jObj = new JSONObject(postResponse);
                error = jObj.getBoolean("error");

                if(!error){
                    String userId = jObj.getString("uid");

                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("name");
                    String email = user.getString("email");

                    AppController.setString(context, "uid", userId);
                    AppController.setString(context, "name", name);
                    AppController.setString(context, "email", email);
                    AppController.setString(context, "password", password);

                } else {
                    errorMsg = jObj.getString("error_msg");

                }

            } catch (JSONException e){
                Log.d("JSON Exception", e.toString());
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
        pDialog.dismiss();
        if (error) {
            Toast.makeText(context,
                    errorMsg, Toast.LENGTH_LONG).show();
        }
        delegate.processFinish(file_url);
    }

    private String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        System.out.println("body: " + body.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        System.out.println("request: "+request);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String register(String name, String token, String email, String password) {
        return "{\"tag\":\"" + "register" + "\","
                + "\"name\":\"" + name + "\","
                + "\"token\":\"" + token + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\"}";
    }
}
