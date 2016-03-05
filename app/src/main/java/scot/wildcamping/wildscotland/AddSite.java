package scot.wildcamping.wildscotland;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddSite extends Activity implements View.OnClickListener {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    EditText Lat;
    EditText Lon;
    EditText title;
    EditText description;
    ImageButton addImage;
    ImageButton addFeature;
    RatingBar rating;
    Button confirmCreation;
    Button cancelCreation;
    double latitude;
    double longitude;
    String latReq;
    String lonReq;
    String titleReq;
    String descReq;
    String ratingReq;
    String url = Appconfig.URL_ADDSITE;
    String postResponse;
    Response response;
    Intent intent;
    String uid;
    int relat = 90;
    Boolean feature1;
    Boolean feature2;
    Boolean feature3;
    Boolean feature4;
    Boolean feature5;
    Boolean feature6;
    Boolean feature7;
    Boolean feature8;
    Boolean feature9;
    Boolean feature10;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        //initializing views
        Lat =(EditText)findViewById(R.id.Lat);
        Lon = (EditText)findViewById(R.id.Long);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);
        addImage = (ImageButton)findViewById(R.id.addImage);
        addFeature = (ImageButton)findViewById(R.id.addFeature);
        rating = (RatingBar)findViewById(R.id.ratingBar);
        confirmCreation = (Button)findViewById(R.id.confirmCreation);
        cancelCreation = (Button)findViewById(R.id.cancelCreation);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            String lat = Double.toString(latitude);
            String lon = Double.toString(longitude);

            Lat.setText(lat);
            Lon.setText(lon);

            feature1 = extras.getBoolean("feature1");
            feature2 = extras.getBoolean("feature2");
            feature3 = extras.getBoolean("feature3");
            feature4 = extras.getBoolean("feature4");
            feature5 = extras.getBoolean("feature5");
            feature6 = extras.getBoolean("feature6");
            feature7 = extras.getBoolean("feature7");
            feature8 = extras.getBoolean("feature8");
            feature9 = extras.getBoolean("feature9");
            feature10 = extras.getBoolean("feature10");

            String titlePassed = extras.getString("title");
            String descPassed = extras.getString("description");

            title.setText(titlePassed);
            description.setText(descPassed);

        }

        //setting onclick listeners
        addImage.setOnClickListener(this);
        addFeature.setOnClickListener(this);
        confirmCreation.setOnClickListener(this);
        cancelCreation.setOnClickListener(this);

    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddSite.this);
            pDialog.setMessage("Adding site ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            uid = AppController.getString(getApplicationContext(), "uid");

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            //String getResponse = doGetRequest(Appconfig.URL_REGISTER);
            //System.out.println(getResponse);

            // issue the post request
            try {
                String json = addSite(uid, relat, latReq, lonReq, titleReq, descReq, ratingReq);
                System.out.println("json: "+json);
                postResponse = doPostRequest(url, json);      //json
                System.out.println("post response: "+postResponse);

                //AppController.setString(AddSite.this, "name", name);
                //AppController.setString(AddSite.this, "email", email);

                /*Intent intent = new Intent(AddSite.this,
                        MainActivity.class);
                startActivity(intent);
                finish();*/

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
            try {
                JSONObject resp = new JSONObject(postResponse);

                boolean error = resp.getBoolean("error");
                if (!error) {

                    Toast.makeText(getApplicationContext(), "Site added!", Toast.LENGTH_LONG).show();

                    intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("add", true);
                    startActivity(intent);
                    finish();

                }else {
                    String errMsg = resp.getString("error_msg");
                    Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e){

            }
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

            System.out.println("body: " + body.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            System.out.println("request: "+request);
            response = client.newCall(request).execute();
            return response.body().string();
        }

        private String addSite(String uid, int relat, String lat, String lon, String title, String description, String rating) {
            return "{\"tag\":\"" + "addSite" + "\","
                    + "\"uid\":\"" + uid + "\","
                    + "\"relat\":\"" + relat + "\","
                    + "\"lat\":\"" + lat + "\","
                    + "\"lon\":\"" + lon + "\","
                    + "\"title\":\"" + title + "\","
                    + "\"description\":\"" + description + "\","
                    + "\"rating\":\"" + rating + "\","
                    + "\"feature1\":\"" + feature1 + "\","
                    + "\"feature2\":\"" + feature2 + "\","
                    + "\"feature3\":\"" + feature3 + "\","
                    + "\"feature4\":\"" + feature4 + "\","
                    + "\"feature5\":\"" + feature5 + "\","
                    + "\"feature6\":\"" + feature6 + "\","
                    + "\"feature7\":\"" + feature7 + "\","
                    + "\"feature8\":\"" + feature8 + "\","
                    + "\"feature9\":\"" + feature9 + "\","
                    + "\"feature10\":\"" + feature10 + "\"}";
        }
    }




    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.addImage:
                //Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;

            case R.id.addFeature:
                Intent intent = new Intent(getApplicationContext(), SelectFeatures.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("feature1", feature1);
                intent.putExtra("feature2", feature2);
                intent.putExtra("feature3", feature3);
                intent.putExtra("feature4", feature4);
                intent.putExtra("feature5", feature5);
                intent.putExtra("feature6", feature6);
                intent.putExtra("feature7", feature7);
                intent.putExtra("feature8", feature8);
                intent.putExtra("feature9", feature9);
                intent.putExtra("feature10", feature10);
                startActivity(intent);
                break;

            case R.id.confirmCreation:

                latReq = Lat.getText().toString();
                lonReq = Lon.getText().toString();
                titleReq = title.getText().toString();
                descReq = description.getText().toString();
                ratingReq = Float.toString(rating.getRating());

                if (!latReq.isEmpty() && !lonReq.isEmpty() && !titleReq.isEmpty() && !descReq.isEmpty() && !ratingReq.isEmpty()) {
                    new CreateNewProduct().execute();

                } else {
                    Snackbar.make(v, "Please enter the details!", Snackbar.LENGTH_LONG).show();
                }


                break;

            case R.id.cancelCreation:

                Toast.makeText(getApplicationContext(), "Site creation canceled!", Toast.LENGTH_LONG).show();

                intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

}