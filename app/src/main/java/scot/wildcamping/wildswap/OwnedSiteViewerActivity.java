package scot.wildcamping.wildswap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import scot.wildcamping.wildswap.Adapters.ImageGalleryAdapter;
import scot.wildcamping.wildswap.AsyncTask.AsyncResponse;
import scot.wildcamping.wildswap.AsyncTask.FetchUsers;
import scot.wildcamping.wildswap.AsyncTask.UpdateSite;
import scot.wildcamping.wildswap.Objects.Gallery;
import scot.wildcamping.wildswap.Objects.Site;
import scot.wildcamping.wildswap.Objects.StoredData;

/**
 * Created by Chris on 26-Feb-16.
 *
 */
public class OwnedSiteViewerActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    int arrayPos;
    Double latitude;
    Double longitude;
    String cid;
    String titleBun;
    String descriptionBun;
    Double ratingBun;
    SparseArray<String> features;
    Boolean feature1 = true;
    Boolean feature2 = true;
    Boolean feature3 = true;
    Boolean feature4 = true;
    Boolean feature5 = true;
    Boolean feature6 = true;
    Boolean feature7 = true;
    Boolean feature8 = true;
    Boolean feature9 = true;
    Boolean feature10 = true;
    String imageStr;
    Bitmap imageBit;
    int prevState;
    SparseArray<Site> owned = new SparseArray<>();
    int RESULT_LOAD_IMAGE = 0;
    SparseArray<Gallery> images;
    Address thisAddress;


    Gallery gallery;
    ArrayList<String> imagesList;

    FrameLayout frame;
    Boolean hasImages = false;
    ImageView close;
    ScrollView scroll;
    ViewPager imageViews;

    ImageView expandedImage;

    TextView lat;
    TextView lon;
    TextView title;
    TextView description;
    RatingBar rating;
    ImageView feature1Image;
    ImageView feature2Image;
    ImageView feature3Image;
    ImageView feature4Image;
    ImageView feature5Image;
    ImageView feature6Image;
    ImageView feature7Image;
    ImageView feature8Image;
    ImageView feature9Image;
    ImageView feature10Image;
    ImageView distantTerrainFeatures;
    ImageView nearbyTerrainFeatures;
    ImageView immediateTerrainFeatures;
    TextView ratedBy;
    LinearLayout featuresBackground;
    TextView country;
    TextView classificationA;
    TextView classificationC;
    TextView classificationE;
    FrameLayout classA;
    FrameLayout classC;
    FrameLayout classE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_viewer);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        lat = (TextView)findViewById(R.id.lat);
        lon = (TextView)findViewById(R.id.lon);
        title = (TextView)findViewById(R.id.siteViewTitle);
        description = (TextView)findViewById(R.id.siteViewDescription);
        rating = (RatingBar)findViewById(R.id.siteViewRating);
        feature1Image = (ImageView)findViewById(R.id.preview_feature1);
        feature2Image = (ImageView)findViewById(R.id.preview_feature2);
        feature3Image = (ImageView)findViewById(R.id.preview_feature3);
        feature4Image = (ImageView)findViewById(R.id.preview_feature4);
        feature5Image = (ImageView)findViewById(R.id.preview_feature5);
        feature6Image = (ImageView)findViewById(R.id.preview_feature6);
        feature7Image = (ImageView)findViewById(R.id.preview_feature7);
        feature8Image = (ImageView)findViewById(R.id.preview_feature8);
        feature9Image = (ImageView)findViewById(R.id.preview_feature9);
        feature10Image = (ImageView)findViewById(R.id.preview_feature10);
        distantTerrainFeatures = (ImageView)findViewById(R.id.distantTerrainFeatures);
        nearbyTerrainFeatures = (ImageView)findViewById(R.id.nearbyTerrainFeatures);
        immediateTerrainFeatures = (ImageView)findViewById(R.id.immediateTerrainFeatures);
        ratedBy = (TextView)findViewById(R.id.ratedBy);
        featuresBackground = (LinearLayout)findViewById(R.id.featuresBackground);
        expandedImage = (ImageView)findViewById(R.id.expanded_image);
        frame = (FrameLayout) findViewById(R.id.frame);
        close = (ImageView)findViewById(R.id.closeImage);
        scroll = (ScrollView)findViewById(R.id.pageScrollView);
        //frame.getForeground().setAlpha(0);
        imageViews = (ViewPager) findViewById(R.id.imageViewPager);
        country = (TextView)findViewById(R.id.country);

        classificationA = (TextView) findViewById(R.id.classificationA);
        classificationC = (TextView) findViewById(R.id.classificationC);
        classificationE = (TextView) findViewById(R.id.classificationE);
        classA = (FrameLayout) findViewById(R.id.classificationAFrame);
        classC = (FrameLayout) findViewById(R.id.classificationCFrame);
        classE = (FrameLayout) findViewById(R.id.classificationEFrame);


        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            arrayPos = extras.getInt("arrayPosition");
            cid = extras.getString("cid");
            prevState = extras.getInt("prevState");

        }

        StoredData inst = new StoredData();
        owned = inst.getOwnedSitesMap();
        Site focused = owned.get(arrayPos);
        cid = focused.getCid();
        latitude = focused.getLat();
        longitude = focused.getLon();
        lat.setText("Latitude: "+latitude.toString());
        lon.setText("Longitude: "+longitude.toString());
        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        rating.setRating((focused.getRating()).floatValue());
        ratedBy.setText("Rated By: " + focused.getRatedBy());

        StoredData ks = new StoredData();
        images = ks.getImages();
        String id = cid.substring(cid.length()-8);
        int cidEnd = Integer.parseInt(id);
        gallery = images.get(cidEnd);

        //if site has no images
        if(gallery.getHasGallery()){
            imagesList = gallery.getGallery();

            ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, imagesList);
            imageViews.setPageMargin(-150);
            imageViews.setAdapter(adapter);
            hasImages = true;

        } else {
            imageViews.setVisibility(View.GONE);
        }

        List<android.location.Address> address = focused.getAddress();
        thisAddress = address.get(0);

        country.setText(thisAddress.getCountryName() + ", " + thisAddress.getLocality());

        if(focused.getDistant().equals("null") && focused.getNearby().equals("null") && focused.getImmediate().equals("null")) {
            distantTerrainFeatures.setVisibility(View.GONE);
            nearbyTerrainFeatures.setVisibility(View.GONE);
            immediateTerrainFeatures.setVisibility(View.GONE);

        } else if(!focused.getDistant().equals("") && !focused.getNearby().equals("") && !focused.getImmediate().equals("")){
            int distantID = this.getResources().getIdentifier("drawable/"+ focused.getDistant(), null, this.getPackageName());
            int nearbyID = this.getResources().getIdentifier("drawable/"+ focused.getNearby(), null, this.getPackageName());
            int immediateID = this.getResources().getIdentifier("drawable/"+ focused.getImmediate(), null, this.getPackageName());

            distantTerrainFeatures.setImageResource(distantID);
            nearbyTerrainFeatures.setImageResource(nearbyID);
            immediateTerrainFeatures.setImageResource(immediateID);

            distantTerrainFeatures.setVisibility(View.VISIBLE);
            nearbyTerrainFeatures.setVisibility(View.VISIBLE);
            immediateTerrainFeatures.setVisibility(View.VISIBLE);

        } else {
            distantTerrainFeatures.setVisibility(View.GONE);
            nearbyTerrainFeatures.setVisibility(View.GONE);
            immediateTerrainFeatures.setVisibility(View.GONE);
        }

        classA.setVisibility(View.GONE);
        classC.setVisibility(View.GONE);
        classE.setVisibility(View.GONE);

        String amateurText = classificationA.getText().toString();
        String casualText = classificationC.getText().toString();
        String expertText = classificationE.getText().toString();

        if(focused.getClassification().isEmpty()){

        } else if(focused.getClassification().equals(amateurText)){
            classA.setVisibility(View.VISIBLE);

        } else if(focused.getClassification().equals(casualText)){
            classC.setVisibility(View.VISIBLE);

        } else if(focused.getClassification().equals(expertText)){
            classE.setVisibility(View.VISIBLE);

        } else {

        }

        /*features = focused.getFeatures();
        for (int i = 0; i<features.unknownSiteSize(); i++){
            if(features.get(i+1).equals("0")){
                String featureID = "feature"+(i+1)+"Image";

                feature1Image.setVisibility(View.GONE);
                feature1 = false;
            }
        }*/

        if(focused.getFeature1().equals("0")){
            feature1Image.setVisibility(View.GONE);
            feature1 = false;
        }
        if(focused.getFeature2().equals("0")){
            feature2Image.setVisibility(View.GONE);
            feature2 = false;
        }
        if(focused.getFeature3().equals("0")){
            feature3Image.setVisibility(View.GONE);
            feature3 = false;
        }
        if(focused.getFeature4().equals("0")){
            feature4Image.setVisibility(View.GONE);
            feature4 = false;
        }
        if(focused.getFeature5().equals("0")){
            feature5Image.setVisibility(View.GONE);
            feature5 = false;
        }
        if(focused.getFeature6().equals("0")){
            feature6Image.setVisibility(View.GONE);
            feature6 = false;
        }
        if(focused.getFeature7().equals("0")){
            feature7Image.setVisibility(View.GONE);
            feature7 = false;
        }
        if(focused.getFeature8().equals("0")){
            feature8Image.setVisibility(View.GONE);
            feature8 = false;
        }
        if(focused.getFeature9().equals("0")){
            feature9Image.setVisibility(View.GONE);
            feature9 = false;
        }
        if(focused.getFeature10().equals("0")){
            feature10Image.setVisibility(View.GONE);
            feature10 = false;
        }

        if(!feature1 && !feature2 && !feature3 && !feature4 && !feature5 && !feature6 && !feature7 && !feature8 && !feature9 && !feature10){
            featuresBackground.setVisibility(View.GONE);
        }

        close.setOnClickListener(this);
    }

    @Override
    public void processFinish(String output){

        StoredData inst = new StoredData();
        owned = inst.getOwnedSitesMap();
        Site focused = owned.get(arrayPos);
        cid = focused.getCid();
        latitude = focused.getLat();
        longitude = focused.getLon();
        lat.setText("Latitude: "+latitude.toString());
        lon.setText("Longitude: "+longitude.toString());
        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        rating.setRating((focused.getRating()).floatValue());
        ratedBy.setText("Rated By: " + focused.getRatedBy());

        StoredData ks = new StoredData();
        images = ks.getImages();
        System.out.println("get images:" + images);
        String id = cid.substring(cid.length()-8);
        int cidEnd = Integer.parseInt(id);
        gallery = images.get(cidEnd);
        System.out.println("cidEnd" + cidEnd);
        System.out.println("gallery" + gallery);
        imagesList = gallery.getGallery();

        LinearLayout layout = (LinearLayout) findViewById(R.id.imagesLinear);

        if(imagesList.isEmpty()){
            //no images
            Log.d("Images", "There are no images");
        } else {
            for(int i = 0; i<imagesList.size(); i++){
                //populate the grid view.
                Log.d("View", "Add View for Image");
                imageBit = StringToBitMap(imagesList.get(i));
                ImageButton imageView = new ImageButton(this);
                imageView.setId(i);
                //imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(imageBit);
                //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setBackgroundColor(Color.TRANSPARENT);
                imageView.setClickable(true);
                //imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
                layout.addView(imageView);
            }
        }

        if(focused.getDistant().equals("null") && focused.getNearby().equals("null") && focused.getImmediate().equals("null")) {
            distantTerrainFeatures.setVisibility(View.GONE);
            nearbyTerrainFeatures.setVisibility(View.GONE);
            immediateTerrainFeatures.setVisibility(View.GONE);

        } else if(!focused.getDistant().equals("") && !focused.getNearby().equals("") && !focused.getImmediate().equals("")){
            int distantID = this.getResources().getIdentifier("drawable/"+ focused.getDistant(), null, this.getPackageName());
            int nearbyID = this.getResources().getIdentifier("drawable/"+ focused.getNearby(), null, this.getPackageName());
            int immediateID = this.getResources().getIdentifier("drawable/"+ focused.getImmediate(), null, this.getPackageName());

            distantTerrainFeatures.setImageResource(distantID);
            nearbyTerrainFeatures.setImageResource(nearbyID);
            immediateTerrainFeatures.setImageResource(immediateID);

            distantTerrainFeatures.setVisibility(View.VISIBLE);
            nearbyTerrainFeatures.setVisibility(View.VISIBLE);
            immediateTerrainFeatures.setVisibility(View.VISIBLE);

        } else {
            distantTerrainFeatures.setVisibility(View.GONE);
            nearbyTerrainFeatures.setVisibility(View.GONE);
            immediateTerrainFeatures.setVisibility(View.GONE);
        }

        /*features = focused.getFeatures();
        for (int i = 0; i<features.unknownSiteSize(); i++){
            if(features.get(i+1).equals("0")){
                String featureID = "feature"+(i+1)+"Image";

                feature1Image.setVisibility(View.GONE);
                feature1 = false;
            }
        }*/

        if(focused.getFeature1().equals("0")){
            feature1Image.setVisibility(View.GONE);
            feature1 = false;
        }
        if(focused.getFeature2().equals("0")){
            feature2Image.setVisibility(View.GONE);
            feature2 = false;
        }
        if(focused.getFeature3().equals("0")){
            feature3Image.setVisibility(View.GONE);
            feature3 = false;
        }
        if(focused.getFeature4().equals("0")){
            feature4Image.setVisibility(View.GONE);
            feature4 = false;
        }
        if(focused.getFeature5().equals("0")){
            feature5Image.setVisibility(View.GONE);
            feature5 = false;
        }
        if(focused.getFeature6().equals("0")){
            feature6Image.setVisibility(View.GONE);
            feature6 = false;
        }
        if(focused.getFeature7().equals("0")){
            feature7Image.setVisibility(View.GONE);
            feature7 = false;
        }
        if(focused.getFeature8().equals("0")){
            feature8Image.setVisibility(View.GONE);
            feature8 = false;
        }
        if(focused.getFeature9().equals("0")){
            feature9Image.setVisibility(View.GONE);
            feature9 = false;
        }
        if(focused.getFeature10().equals("0")){
            feature10Image.setVisibility(View.GONE);
            feature10 = false;
        }

        if(!feature1 && !feature2 && !feature3 && !feature4 && !feature5 && !feature6 && !feature7 && !feature8 && !feature9 && !feature10){
            featuresBackground.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.imageViewPager:

                imageBit = StringToBitMap(gallery.getGallery().get(0));
                expandedImage.setImageBitmap(imageBit);
                expandedImage.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                //frame.getForeground().setAlpha(150);

                break;

            case R.id.closeImage:

                expandedImage.setVisibility(View.INVISIBLE);
                close.setVisibility(View.GONE);
                //frame.getForeground().setAlpha(0);
                scroll.setClickable(true);
                break;
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

                //Intent intent = null;
                if(prevState == 1) {
                    Intent intent = new Intent(getApplicationContext(),SitesActivity.class);
                    intent.putExtra("fragment", 1);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
                return true;

            case R.id.showOnMap:
                Intent intent = new Intent(this, MainActivity_Spinner.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("add", true);
                startActivity(intent);
                finish();

                break;

            case R.id.gift:

                ArrayList<String> emails = new ArrayList<>();
                final Intent i = new Intent(this, GiftSiteActivity.class);
                i.putExtra("cid", cid);
                new FetchUsers(this, emails, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        startActivity(i);
                    }
                }).execute();

                break;

            case R.id.delete:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Attention!");
                builder1.setMessage("Are you sure you want to delete this site?");

                builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        boolean active = false;
                        //String ratingReq = Double.toString(ratingBun);
                        final Intent intent = new Intent(getApplicationContext(), MainActivity_Spinner.class);
                        //trigger php to deactivate site
                        new UpdateSite(getApplicationContext(), true, active, cid, null, null, null, null, null, null, null, null, null, null, null, null, null, imageStr, 0, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                startActivity(intent);
                                finish();
                            }
                        }).execute();

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

            case R.id.update:
                intent = new Intent(getApplicationContext(),UpdateSiteViewerActivity.class);
                //bundle all current details into "add site"
                intent.putExtra("arrayPosition", arrayPos);
                intent.putExtra("image", hasImages);

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
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.owned_site_viewer, menu);
        return true;
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

}
