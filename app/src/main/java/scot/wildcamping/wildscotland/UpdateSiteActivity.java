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
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class UpdateSiteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Lat;
    EditText Lon;
    EditText title;
    EditText description;
    RelativeLayout addPhoto;
    RelativeLayout addFeatures;
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
    String url = Appconfig.URL;
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

    Boolean update = true;
    Boolean lonLat;

    int arrayPos;

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
        addPhoto = (RelativeLayout)findViewById(R.id.addPhotoRel);
        addFeatures = (RelativeLayout)findViewById(R.id.addFeaturesRel);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        confirmCreation = (Button)findViewById(R.id.confirmCreation);
        cancelCreation = (Button)findViewById(R.id.cancelCreation);
        image1 = (ImageView)findViewById(R.id.image1);

        //title.setSelection(0);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            arrayPos = extras.getInt("arrayPosition");

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

            if (feature1 || feature2 || feature3 || feature4 || feature5 || feature6 || feature7 || feature8 || feature9 || feature10) {
                addFeatures.setBackgroundColor(green);
            } else {
                addFeatures.setBackgroundColor(gray);
            }

            titlePassed = extras.getString("title");
            descPassed = extras.getString("description");

            title.setText(titlePassed);
            description.setText(descPassed);

            rating = extras.getDouble("rating");

            float ratingFloat = (float)rating;
            ratingBar.setRating(ratingFloat);

        }

        knownSite inst = new knownSite();
        Site focused = inst.getOwnedSitesMap().get(arrayPos);

        cid = focused.getCid();
        Lat.setText(Double.toString(focused.getPosition().latitude));
        Lon.setText(Double.toString(focused.getPosition().latitude));
        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        if(focused.getImage() != null) {
            image = focused.getImage();
            image1.setVisibility(View.VISIBLE);
            image1.setImageBitmap(StringToBitMap(image));
        }
        feature1.equals(focused.getFeature1());
        feature2.equals(focused.getFeature2());
        feature3.equals(focused.getFeature3());
        feature4.equals(focused.getFeature4());
        feature5.equals(focused.getFeature5());
        feature6.equals(focused.getFeature6());
        feature7.equals(focused.getFeature7());
        feature8.equals(focused.getFeature8());
        feature9.equals(focused.getFeature9());
        feature10.equals(focused.getFeature10());
        ratingBar.setRating((focused.getRating()).floatValue());

        //setting onclick listeners
        addPhoto.setOnClickListener(this);
        addFeatures.setOnClickListener(this);
        confirmCreation.setOnClickListener(this);
        cancelCreation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.addPhotoRel:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                break;

            case R.id.addFeaturesRel:

                Intent intent = new Intent(getApplicationContext(), SelectFeatures.class);
                intent.putExtra("arrayPosition", arrayPos);
                intent.putExtra("update", update);
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
                ratingReq = Float.toString(ratingBar.getRating());

                //feature bools to string here for updated values

                if (!latReq.isEmpty() && !lonReq.isEmpty() && !titleReq.isEmpty() && !descReq.isEmpty() && !ratingReq.isEmpty()) {

                    String imageSingleLine = null;
                    boolean active = true;
                    if(image != null) {
                        imageSingleLine = image.replaceAll("[\r\n]+", "");
                    }

                    new UpdateSite(this, true, active, cid, titleReq, descReq, ratingReq, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, imageSingleLine, 0).execute();

                    //intent = new Intent(getApplicationContext(),
                            //MainActivity_Spinner.class);
                    //intent.putExtra("latitude", latitude);
                    //intent.putExtra("longitude", longitude);
                    //intent.putExtra("add", true);
                    //startActivity(intent);
                    finish();

                } else {
                    Snackbar.make(v, "Please enter the details!", Snackbar.LENGTH_LONG).show();
                }

                break;

            case R.id.cancelCreation:

                Toast.makeText(getApplicationContext(), "Site update canceled!", Toast.LENGTH_LONG).show();

                intent = new Intent(getApplicationContext(),OwnedSiteActivity.class);
                intent.putExtra("arrayPosition", arrayPos);
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

                Toast.makeText(getApplicationContext(), "Site update canceled!", Toast.LENGTH_LONG).show();

                intent = new Intent(getApplicationContext(),OwnedSiteActivity.class);
                intent.putExtra("arrayPosition", arrayPos);
                startActivity(intent);

                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}