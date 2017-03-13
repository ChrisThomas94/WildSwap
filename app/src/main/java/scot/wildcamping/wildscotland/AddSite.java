package scot.wildcamping.wildscotland;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.model.Gallery;
import scot.wildcamping.wildscotland.model.knownSite;

public class AddSite extends AppCompatActivity implements View.OnClickListener {

    EditText Lat;
    EditText Lon;
    EditText title;
    EditText description;
    RelativeLayout addImage;
    RelativeLayout siteBuilder;
    RelativeLayout addFeature;
    TextView or;

    ImageView image1;
    ImageView image2;
    ImageView image3;

    ImageView distantTerrainFeatures;
    ImageView nearbyTerrainFeatures;
    ImageView immediateTerrainFeatures;

    RatingBar ratingBar;
    Button confirmCreation;
    double latitude;
    double longitude;
    String latReq;
    String lonReq;
    String titleReq;
    String descReq;
    String ratingReq;
    float rating;
    String distant;
    String nearby;
    String immediate;
    CheckBox imagePermission;
    Boolean permission;
    String url = Appconfig.URL;
    Intent intent;
    int RESULT_LOAD_IMAGE = 0;
    Uri targetUri;
    String imageMultiLine;

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


    int relat = 90;
    Boolean update;
    Boolean lonLat;
    Boolean imageUpload = false;
    knownSite inst = new knownSite();
    int tempLocation;

    Uri uri1;
    Uri uri2;
    Uri uri3;

    SparseArray<Uri> imagesUri = new SparseArray<>();
    SparseArray<String> image = new SparseArray<>();
    String[] imageUris = new String[3];
    SparseArray<Gallery> temp = new SparseArray<>();

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
        siteBuilder = (RelativeLayout)findViewById(R.id.siteBuilder);
        addFeature = (RelativeLayout)findViewById(R.id.addFeaturesRel);
        imagePermission = (CheckBox)findViewById(R.id.imagePermission);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        confirmCreation = (Button)findViewById(R.id.confirmCreation);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);

        or = (TextView)findViewById(R.id.or);

        distantTerrainFeatures = (ImageView)findViewById(R.id.distantTerrainFeatures);
        nearbyTerrainFeatures = (ImageView)findViewById(R.id.nearbyTerrainFeatures);
        immediateTerrainFeatures = (ImageView)findViewById(R.id.immediateTerrainFeatures);

        distantTerrainFeatures.setVisibility(View.GONE);
        nearbyTerrainFeatures.setVisibility(View.GONE);
        immediateTerrainFeatures.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");

            String lat = Double.toString(latitude);
            String lon = Double.toString(longitude);

            Lat.setText(lat);
            Lon.setText(lon);

            distant = extras.getString("distantTerrain");
            nearby = extras.getString("nearbyTerrain");
            immediate = extras.getString("immediateTerrain");

            if(distant != null && nearby != null && immediate != null){
                int distantID = this.getResources().getIdentifier("drawable/"+ distant, null, this.getPackageName());
                int nearbyID = this.getResources().getIdentifier("drawable/"+ nearby, null, this.getPackageName());
                int immediateID = this.getResources().getIdentifier("drawable/"+ immediate, null, this.getPackageName());

                distantTerrainFeatures.setImageResource(distantID);
                nearbyTerrainFeatures.setImageResource(nearbyID);
                immediateTerrainFeatures.setImageResource(immediateID);

                distantTerrainFeatures.setVisibility(View.VISIBLE);
                nearbyTerrainFeatures.setVisibility(View.VISIBLE);
                immediateTerrainFeatures.setVisibility(View.VISIBLE);

                addImage.setVisibility(View.GONE);
                or.setVisibility(View.GONE);
            }

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
                addFeature.setBackgroundResource(R.drawable.rounded_green_button);
            } else {
                addFeature.setBackgroundResource(R.drawable.rounded_grey_button);
            }

            titlePassed = extras.getString("title");
            descPassed = extras.getString("description");

            title.setText(titlePassed);
            description.setText(descPassed);

            imageUpload = extras.getBoolean("image");

            if(imageUpload) {
                imageUris = extras.getStringArray("images");
            }

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

            for(int i=0; i<imageUris.length; i++){

                Uri myUri = Uri.parse(imageUris[i]);

                try {
                    compress = BitmapFactory.decodeStream(getContentResolver().openInputStream(myUri));
                    image.append(i, getStringImage(compress));


                    System.out.println("image being passed:" + imageUris[i]);

                    if (i == 0) {
                        image1.setVisibility(View.VISIBLE);
                        image1.setImageBitmap(compress);
                        siteBuilder.setVisibility(View.GONE);
                        or.setVisibility(View.GONE);
                    } else if (i == 1) {
                        image2.setVisibility(View.VISIBLE);
                        image2.setImageBitmap(compress);
                    } else if (i == 2) {
                        image3.setVisibility(View.VISIBLE);
                        image3.setImageBitmap(compress);
                    }
                }
                catch (FileNotFoundException f){

                }
            }

        }

        //setting onclick listeners
        addImage.setOnClickListener(this);
        siteBuilder.setOnClickListener(this);
        addFeature.setOnClickListener(this);
        confirmCreation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.addPhotoRel:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                break;

            case R.id.siteBuilder:

                Intent siteBuilder = new Intent(getApplicationContext(), SiteBuilder.class);
                siteBuilder.putExtra("image", imageUpload);
                //intent.putExtra("temp", tempLocation);
                siteBuilder.putExtra("latitude", latitude);
                siteBuilder.putExtra("longitude", longitude);
                siteBuilder.putExtra("title", title.getText().toString());
                siteBuilder.putExtra("description", description.getText().toString());
                siteBuilder.putExtra("rating", ratingBar.getRating());
                startActivity(siteBuilder);
                break;

            case R.id.addFeaturesRel:

                Intent intent = new Intent(getApplicationContext(), FeaturesActivity.class);
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
                intent.putExtra("images", imageUris);
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
                        permission = imagePermission.isChecked();

                        if (!latReq.isEmpty() && !lonReq.isEmpty() && !titleReq.isEmpty() && !descReq.isEmpty() && !ratingReq.isEmpty()) {

                            //String imageSingleLine1 = null;
                            //String imageSingleLine2 = null;
                            //String imageSingleLine3 = null;

                            //init number of images to be sent
                            String[] imagesSingleLine = new String[3];
                            if(image.size() != 0) {
                                for(int i = 0; i< image.size(); i++){
                                    imagesSingleLine[i] = image.get(i).toString().replaceAll("[\r\n]+", "");
                                }
                                //imageSingleLine1 = image.replaceAll("[\r\n]+", "");
                            }

                            titleReq = titleReq.replace("'", "\'");
                            descReq = descReq.replace("'", "\'");
                            //titleReq = DatabaseUtils.sqlEscapeString(titleReq);
                            //descReq = DatabaseUtils.sqlEscapeString(descReq);

                            try {
                                String newSite = new CreateSite(getApplicationContext(), relat, latReq, lonReq, titleReq, descReq, ratingReq, permission, distant, nearby, immediate, feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8, feature9, feature10, imagesSingleLine).execute().get();
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

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            ClipData clip = data.getClipData();

            if (clip == null) {
                Uri uri = data.getData();
                imagesUri.append(0, uri);
                imageUris[0] = uri.toString();
                System.out.println("image uri end:" + imageUris[0]);

            } else {

                System.out.println("clip data: " + clip.getItemCount());

                for (int i = 0; i < clip.getItemCount(); i++) {
                    ClipData.Item item = clip.getItemAt(i);

                    Uri uri = item.getUri();

                    imagesUri.append(i, uri);
                    imageUris[i] = uri.toString();

                    System.out.println("image uri end:" + imageUris[i]);

                }
            }
        }

        System.out.println("targetUri " + imagesUri.size());

        for(int i = 0; i < imagesUri.size(); i++) {
            System.out.println("image " + imagesUri.get(i));

            try {
                compress = BitmapFactory.decodeStream(getContentResolver().openInputStream(imagesUri.get(i)));
                //compress = Bitmap.createScaledBitmap(bitmap, 750, 750, true);
                image.append(i, getStringImage(compress));
                imageUpload = true;


                Gallery upload = new Gallery();
                upload.setImage1(image.get(i));
                upload.setCid("temp"+i);

                temp = inst.getTemp();
                temp.put(i, upload);
                inst.setTemp(temp);

                if(i == 0){
                    image1.setVisibility(View.VISIBLE);
                    image1.setImageBitmap(compress);
                    siteBuilder.setVisibility(View.GONE);
                    or.setVisibility(View.GONE);
                } else if(i == 1){
                    image2.setVisibility(View.VISIBLE);
                    image2.setImageBitmap(compress);
                } else if(i == 2){
                    image3.setVisibility(View.VISIBLE);
                    image3.setImageBitmap(compress);
                }

            } catch (FileNotFoundException e) {
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