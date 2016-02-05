package scot.wildcamping.wildscotland;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSite extends Activity {

    EditText Lat;
    EditText Long;
    ImageButton addImage;
    ImageButton addFeature;
    TextInputLayout title;
    TextInputLayout description;
    RatingBar rating;
    Button confirmCreation;
    Button cancelCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        Lat =(EditText)findViewById(R.id.Lat);
        Long = (EditText)findViewById(R.id.Long);
        addImage = (ImageButton)findViewById(R.id.addImage);
        addFeature = (ImageButton)findViewById(R.id.addFeature);
        title = (TextInputLayout)findViewById(R.id.addTitle);
        description = (TextInputLayout)findViewById(R.id.addDescription);
        rating = (RatingBar)findViewById(R.id.ratingBar);
        confirmCreation = (Button)findViewById(R.id.confirmCreation);
        cancelCreation = (Button)findViewById(R.id.cancelCreation);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            double latitude = extras.getDouble("latitude");
            double longitude = extras.getDouble("longitude");

            String lat = Double.toString(latitude);
            String lon = Double.toString(longitude);

            Lat.setText(lat);
            Long.setText(lon);

        }






    }


}