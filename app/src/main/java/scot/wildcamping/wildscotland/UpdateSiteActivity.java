package scot.wildcamping.wildscotland;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UpdateSiteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Lat;
    EditText Lon;
    EditText title;
    EditText description;
    ImageButton addImage;
    ImageButton addFeature;
    ImageView image1;
    RatingBar ratingBar;
    Button confirmCreation;
    Button cancelCreation;
    double latitude;
    double longitude;
    String latReq;
    String lonReq;
    String titleReq;
    String descReq;
    String ratingReq;
    double rating;
    String url = Appconfig.URL_ADDSITE;
    Intent intent;
    int RESULT_LOAD_IMAGE = 0;
    Uri targetUri;
    String image;
    String imageMultiLine;
    String cid;

    String titlePassed;
    String descPassed;

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

    String feature1String;
    String feature2String;
    String feature3String;
    String feature4String;
    String feature5String;
    String feature6String;
    String feature7String;
    String feature8String;
    String feature9String;
    String feature10String;

    Boolean update = true;
    Boolean lonLat;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_site);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);


        final int green = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        final int gray = ContextCompat.getColor(getApplicationContext(), R.color.counter_text_color);

        //initializing views
        Lat =(EditText)findViewById(R.id.Lat);
        Lon = (EditText)findViewById(R.id.Long);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);
        addImage = (ImageButton)findViewById(R.id.addImage);
        addFeature = (ImageButton)findViewById(R.id.addFeature);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        confirmCreation = (Button)findViewById(R.id.confirmCreation);
        cancelCreation = (Button)findViewById(R.id.cancelCreation);
        image1 = (ImageView)findViewById(R.id.image1);

        //title.setSelection(0);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {

            lonLat = extras.getBoolean("lonLat");
            if(lonLat){
                //make text editable
            }

            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            String lat = Double.toString(latitude);
            String lon = Double.toString(longitude);

            Lat.setText(lat);
            Lon.setText(lon);

            image = extras.getString("image");
            image1.setImageBitmap(StringToBitMap(image));

            feature1String = extras.getString("feature1");
            feature2String = extras.getString("feature2");
            feature3String = extras.getString("feature3");
            feature4String = extras.getString("feature4");
            feature5String = extras.getString("feature5");
            feature6String = extras.getString("feature6");
            feature7String = extras.getString("feature7");
            feature8String = extras.getString("feature8");
            feature9String = extras.getString("feature9");
            feature10String = extras.getString("feature10");

            feature1 = extras.getBoolean("feature1Bool");
            feature2 = extras.getBoolean("feature2Bool");
            feature3 = extras.getBoolean("feature3Bool");
            feature4 = extras.getBoolean("feature4Bool");
            feature5 = extras.getBoolean("feature5Bool");
            feature6 = extras.getBoolean("feature6Bool");
            feature7 = extras.getBoolean("feature7Bool");
            feature8 = extras.getBoolean("feature8Bool");
            feature9 = extras.getBoolean("feature9Bool");
            feature10 = extras.getBoolean("feature10Bool");

            if (feature1 || feature2 || feature3 || feature4 || feature5 || feature6 || feature7 || feature8 || feature9 || feature10) {
                addFeature.setBackgroundColor(green);
            } else {
                addFeature.setBackgroundColor(gray);
            }

            titlePassed = extras.getString("title");
            descPassed = extras.getString("description");

            title.setText(titlePassed);
            description.setText(descPassed);

            rating = extras.getDouble("rating");

            float ratingFloat = extras.getFloat("rating");
            ratingBar.setRating(ratingFloat);

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
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                break;

            case R.id.addFeature:

                Intent intent = new Intent(getApplicationContext(), SelectFeatures.class);
                intent.putExtra("update", update);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("image", image);
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
                intent.putExtra("rating", rating);
                startActivity(intent);
                break;

            case R.id.confirmCreation:

                latReq = Lat.getText().toString();
                lonReq = Lon.getText().toString();
                titleReq = title.getText().toString();
                descReq = description.getText().toString();
                ratingReq = Float.toString(ratingBar.getRating());

                //feature bools to string here for updated values

                if (!latReq.isEmpty() && !lonReq.isEmpty() && !titleReq.isEmpty() && !descReq.isEmpty() && !ratingReq.isEmpty()) {

                    boolean active = true;
                    new UpdateSite(this, active, cid, titlePassed, descPassed, rating, feature1String, feature2String, feature3String, feature4String, feature5String, feature6String, feature7String, feature8String, feature9String, feature10String).execute();
                    //new UploadImage(this, imageSingleLine, null).execute();

                    intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("add", true);
                    startActivity(intent);
                    finish();

                } else {
                    Snackbar.make(v, "Please enter the details!", Snackbar.LENGTH_LONG).show();
                }

                break;

            case R.id.cancelCreation:

                Toast.makeText(getApplicationContext(), "Site update canceled!", Toast.LENGTH_LONG).show();

                intent = new Intent(getApplicationContext(),OwnedSiteActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("image", image);
                //intent.putExtra("feature1", feature1String);
                //intent.putExtra("feature2", feature2String);
                //intent.putExtra("feature3", feature3String);
                //intent.putExtra("feature4", feature4String);
                //intent.putExtra("feature5", feature5String);
                //intent.putExtra("feature6", feature6String);
                //intent.putExtra("feature7", feature7String);
                //intent.putExtra("feature8", feature8String);
                //intent.putExtra("feature9", feature9String);
                //intent.putExtra("feature10", feature10String);
                intent.putExtra("title", titlePassed);
                intent.putExtra("description", descPassed);
                intent.putExtra("rating", rating);
                startActivity(intent);

                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            targetUri = data.getData();
            //textTargetUri.setText(targetUri.toString());
            try{
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                image = getStringImage(bitmap);
                image1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
    }

    public String getStringImage(Bitmap bmp){
        if(bmp != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return null;
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                Intent intent;
                intent = new Intent(getApplicationContext(),OwnedSiteActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("image", image);
                intent.putExtra("feature1", feature1String);
                intent.putExtra("feature2", feature2String);
                intent.putExtra("feature3", feature3String);
                intent.putExtra("feature4", feature4String);
                intent.putExtra("feature5", feature5String);
                intent.putExtra("feature6", feature6String);
                intent.putExtra("feature7", feature7String);
                intent.putExtra("feature8", feature8String);
                intent.putExtra("feature9", feature9String);
                intent.putExtra("feature10", feature10String);
                intent.putExtra("title", titlePassed);
                intent.putExtra("description", descPassed);
                intent.putExtra("rating", rating);
                startActivity(intent);

                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}