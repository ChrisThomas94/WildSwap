package scot.wildcamping.wildswap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import scot.wildcamping.wildswap.AsyncTask.CreateNotification;
import scot.wildcamping.wildswap.AsyncTask.FetchKnownSites;
import scot.wildcamping.wildswap.AsyncTask.ReportSite;
import scot.wildcamping.wildswap.AsyncTask.UpdateSite;
import scot.wildcamping.wildswap.Objects.Gallery;
import scot.wildcamping.wildswap.Objects.Site;
import scot.wildcamping.wildswap.Objects.StoredData;

/**
 * Created by Chris on 26-Feb-16.
 *
 */
public class KnownSiteViewerActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    int arrayPos;
    Double latitude;
    Double longitude;
    String cid;
    String imageStr;
    Bitmap imageBit;
    int prevState;
    SparseArray<Site> known = new SparseArray<>();
    SparseArray<Gallery> images;
    Address thisAddress;
    double ratingOnStart;

    Gallery gallery;
    ArrayList<String> imagesList;

    FrameLayout frame;
    Boolean hasImages = false;
    ImageView close;
    ScrollView scroll;
    ViewPager imageViews;

    ImageView expandedImage;
    View parentLayout;

    TextView lat;
    TextView lon;
    TextView title;
    TextView description;
    RatingBar rating;
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

    Boolean showDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_viewer);

        parentLayout = findViewById(android.R.id.content);


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        lat = (TextView)findViewById(R.id.lat);
        lon = (TextView)findViewById(R.id.lon);
        title = (TextView)findViewById(R.id.siteViewTitle);
        description = (TextView)findViewById(R.id.siteViewDescription);
        rating = (RatingBar)findViewById(R.id.siteViewRating);
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

        System.out.println("cid1" + cid);

        StoredData inst = new StoredData();
        known = inst.getKnownSitesMap();
        Site focused = known.get(arrayPos);
        cid = focused.getCid();
        System.out.println("cid" + cid);
        latitude = focused.getLat();
        longitude = focused.getLon();
        lat.setText("Latitude: "+latitude.toString());
        lon.setText("Longitude: "+longitude.toString());
        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        rating.setRating((focused.getRating()).floatValue());
        ratedBy.setText("Rated By: " + focused.getRatedBy());

        rating.setClickable(true);
        rating.setIsIndicator(false);
        ratingOnStart = rating.getRating();
        System.out.println("rating on start" + ratingOnStart);

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
            imageViews.setOffscreenPageLimit(imagesList.size()-1);
            hasImages = true;

        } else {
            imageViews.setVisibility(View.GONE);
        }

        List<Address> address = focused.getAddress();
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

        close.setOnClickListener(this);
    }

    @Override
    public void processFinish(String output){

        StoredData inst = new StoredData();
        known = inst.getOwnedSitesMap();
        Site focused = known.get(arrayPos);
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

                double ratingOnExit = rating.getRating();

                if(ratingOnExit != ratingOnStart){

                    String newRating = Double.toString(ratingOnExit);

                    new UpdateSite(this, false, true, cid, null, null, newRating, null, null, null, null, null, null, null, null, null, null, null, 0, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {

                            new FetchKnownSites(KnownSiteViewerActivity.this, showDialog, new AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    if(prevState == 1) {
                                        Intent intent = new Intent(getApplicationContext(),SitesActivity.class);
                                        intent.putExtra("fragment", 1);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        finish();
                                    }
                                }
                            }).execute();
                        }
                    }).execute();

                } else {
                    if(prevState == 1) {
                        Intent intent = new Intent(getApplicationContext(),SitesActivity.class);
                        intent.putExtra("fragment", 1);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
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

            case R.id.report:

                new CreateNotification(this, Appconfig.myToken, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        new ReportSite(KnownSiteViewerActivity.this, cid, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {

                                BadgeManager bm = new BadgeManager(KnownSiteViewerActivity.this);
                                bm.checkReportedBadges();
                                Snackbar.make(parentLayout, "This site has been reported to the administrator.", Snackbar.LENGTH_LONG).show();

                            }
                        }).execute();
                    }
                }).execute();

                /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(KnownSiteViewerActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }*/

                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.known_site_viewer, menu);
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
