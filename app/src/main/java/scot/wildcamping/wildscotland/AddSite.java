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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSite extends Activity implements View.OnClickListener {

    EditText Lat;
    EditText Long;
    ImageButton addImage;
    ImageButton addFeature;
    TextInputLayout title;
    TextInputLayout description;
    RatingBar rating;
    Button confirmCreation;
    Button cancelCreation;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

        //initializing views
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
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            String lat = Double.toString(latitude);
            String lon = Double.toString(longitude);

            Lat.setText(lat);
            Long.setText(lon);

        }

        //setting onclick listeners
        addImage.setOnClickListener(this);
        addFeature.setOnClickListener(this);
        confirmCreation.setOnClickListener(this);
        cancelCreation.setOnClickListener(this);

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
                break;

            case R.id.confirmCreation:
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("add", true);
                startActivity(intent);
                finish();
                break;

            case R.id.cancelCreation:
                Intent intent2 = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent2);
                finish();
                break;
        }

    }

}