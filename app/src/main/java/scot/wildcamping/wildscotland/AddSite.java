package scot.wildcamping.wildscotland;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.model.Image;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class AddSite extends AppCompatActivity implements View.OnClickListener {

    EditText Lat;
    EditText Lon;
    EditText title;
    EditText description;
    RelativeLayout addImage;
    RelativeLayout addFeature;
    ImageView image1;
    ImageView image2;
    ImageView image3;
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
    float rating;
    String url = Appconfig.URL;
    Intent intent;
    int RESULT_LOAD_IMAGE = 0;
    Uri targetUri;
    String image;
    String imageMultiLine;

    String titlePassed;
    String descPassed;

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
    Boolean update;
    Boolean lonLat;
    Boolean imageUpload = false;
    knownSite inst = new knownSite();
    int tempLocation;
    SparseArray<Image> temp = new SparseArray<>();


    private Bitmap bitmap;
    Bitmap compress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);

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
        addImage = (RelativeLayout)findViewById(R.id.addPhotoRel);
        addFeature = (RelativeLayout)findViewById(R.id.addFeaturesRel);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        confirmCreation = (Button)findViewById(R.id.confirmCreation);
        cancelCreation = (Button)findViewById(R.id.cancelCreation);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            imageUpload = extras.getBoolean("image");
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

            if (feature1 || feature2 || feature3 || feature4 || feature5 || feature6 || feature7 || feature8 || feature9 || feature10) {
                addFeature.setBackgroundColor(green);
            } else {
                addFeature.setBackgroundColor(gray);
            }

            titlePassed = extras.getString("title");
            descPassed = extras.getString("description");

            title.setText(titlePassed);
            description.setText(descPassed);

            rating = extras.getFloat("rating");

            //float ratingFloat = (float)rating;
            ratingBar.setRating(rating);

        }

        if(titlePassed == null){
            title.setText("Cool wild location");
        }

        if(descPassed == null){
            description.setText("It is really cool here...");
        }

        System.out.println(imageUpload);


        if(imageUpload) {
            temp = inst.getTemp();
            image = temp.get(0).getImage();
            System.out.println(image);
            compress = StringToBitMap(image);
            image1.setVisibility(View.VISIBLE);
            image1.setImageBitmap(compress);
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
            case R.id.addPhotoRel:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                break;

            case R.id.addFeaturesRel:

                Intent intent = new Intent(getApplicationContext(), SelectFeatures.class);
                intent.putExtra("image", imageUpload);
                //intent.putExtra("temp", tempLocation);
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
                intent.putExtra("rating", ratingBar.getRating());
                startActivity(intent);
                break;

            case R.id.confirmCreation:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddSite.this);
                builder1.setTitle("Attention!");
                builder1.setMessage("The exact location of this site will not be visible to any other users until you trade it with them. Please only add locations where access rights can be exercised, as outlined in the Scottish Outdoor Access Code. If not your location may be at risk of being removed and your account banned, you can remove your location at any time.");

                builder1.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        latReq = Lat.getText().toString();
                        lonReq = Lon.getText().toString();
                        titleReq = title.getText().toString();
                        descReq = description.getText().toString();
                        ratingReq = Float.toString(ratingBar.getRating());

                        if (!latReq.isEmpty() && !lonReq.isEmpty() && !titleReq.isEmpty() && !descReq.isEmpty() && !ratingReq.isEmpty()) {

                            String imageSingleLine = null;
                            if(image != null) {
                                imageSingleLine = image.replaceAll("[\r\n]+", "");
                            }

                            titleReq = titleReq.replace("'", "\'");
                            descReq = descReq.replace("'", "\'");
                            //titleReq = DatabaseUtils.sqlEscapeString(titleReq);
                            //descReq = DatabaseUtils.sqlEscapeString(descReq);

                            try {
                                String newSite = new CreateSite(getApplicationContext(), relat, latReq, lonReq, titleReq, descReq, ratingReq, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, imageSingleLine).execute().get();
                            } catch (ExecutionException e){

                            } catch (InterruptedException e){

                            }
                            Intent intent = new Intent(getApplicationContext(),
                                    MainActivity_Spinner.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("add", true);
                            startActivity(intent);
                            finish();

                        } else {
                            //Snackbar.make(v, "Please enter the details!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert1 = builder1.create();
                alert1.show();



                /*LayoutInflater layoutInflater = (LayoutInflater)this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupView = layoutInflater.inflate(R.layout.popup_add_site, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);

                Button cancel = (Button) popupView.findViewById(R.id.cancelPopup);
                Button accept = (Button) popupView.findViewById(R.id.acceptPopup);

                cancel.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                accept.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        latReq = Lat.getText().toString();
                        lonReq = Lon.getText().toString();
                        titleReq = title.getText().toString();
                        descReq = description.getText().toString();
                        ratingReq = Float.toString(ratingBar.getRating());

                        if (!latReq.isEmpty() && !lonReq.isEmpty() && !titleReq.isEmpty() && !descReq.isEmpty() && !ratingReq.isEmpty()) {

                            String imageSingleLine = null;
                            if(image != null) {
                                imageSingleLine = image.replaceAll("[\r\n]+", "");
                            }

                            new CreateSite(getApplicationContext(), relat, latReq, lonReq, titleReq, descReq, ratingReq, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, imageSingleLine).execute();

                            Intent intent = new Intent(getApplicationContext(),
                                    MainActivity_Spinner.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("add", true);
                            startActivity(intent);
                            finish();

                        } else {
                            Snackbar.make(v, "Please enter the details!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });*/

                break;

            case R.id.cancelCreation:

                Toast.makeText(getApplicationContext(), "Site creation canceled!", Toast.LENGTH_LONG).show();

                intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                intent.putExtra("add", false);
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
                compress = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                //compress = Bitmap.createScaledBitmap(bitmap, 750, 750, true);
                image = getStringImage(compress);
                imageUpload = true;

                Image upload = new Image();
                upload.setImage(image);
                upload.setCid("temp");

                temp = inst.getTemp();


                temp.put(0, upload);

                inst.setTemp(temp);

                image1.setVisibility(View.VISIBLE);
                image1.setImageBitmap(compress);
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
    public void onBackPressed() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                Toast.makeText(getApplicationContext(), "Site creation canceled!", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), MainActivity_Spinner.class);
                intent.putExtra("add", false);
                startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}