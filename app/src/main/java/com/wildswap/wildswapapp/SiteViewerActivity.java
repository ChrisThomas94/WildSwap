package com.wildswap.wildswapapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.wildswap.wildswapapp.Adapters.ImageGalleryAdapter;
import com.wildswap.wildswapapp.Adapters.ViewPagerAdapter;
import com.wildswap.wildswapapp.Adapters.WrapContentHeightViewPager;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.CreateNotification;
import com.wildswap.wildswapapp.AsyncTask.FetchKnownSites;
import com.wildswap.wildswapapp.AsyncTask.FetchUsers;
import com.wildswap.wildswapapp.AsyncTask.ReportSite;
import com.wildswap.wildswapapp.AsyncTask.UpdateSite;
import com.wildswap.wildswapapp.Objects.Gallery;
import com.wildswap.wildswapapp.Objects.Site;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 26-Feb-16.
 *
 */
public class SiteViewerActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    int arrayPos;
    Double latitude;
    Double longitude;
    String cid;
    Bitmap imageBit;
    int prevState;
    SparseArray<Site> sites = new SparseArray<>();
    SparseArray<Gallery> images;
    Address thisAddress;
    double ratingOnStart;

    Gallery gallery;
    ArrayList<String> imagesList;

    FrameLayout frame;
    Boolean hasImages = false;
    ScrollView scroll;
    ViewPager imageViews;
    boolean showDialog = true;
    boolean owned = false;
    View parentLayout;

    Site focused;

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
    ImageView suited;
    ImageButton googleMap;

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Guardians", "News"};
    int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_viewer);

        parentLayout = findViewById(android.R.id.content);


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles, Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        StoredData inst = new StoredData();
        SparseArray<User> fetchedUsers = inst.getGuardians();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pager.getLayoutParams();
        params.height = 360*fetchedUsers.size();
        pager.setLayoutParams(params);

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
        frame = (FrameLayout) findViewById(R.id.frame);
        scroll = (ScrollView)findViewById(R.id.pageScrollView);
        imageViews = (ViewPager) findViewById(R.id.imageViewPager);
        country = (TextView)findViewById(R.id.country);
        classificationA = (TextView) findViewById(R.id.classificationA);
        classificationC = (TextView) findViewById(R.id.classificationC);
        classificationE = (TextView) findViewById(R.id.classificationE);
        classA = (FrameLayout) findViewById(R.id.classificationAFrame);
        classC = (FrameLayout) findViewById(R.id.classificationCFrame);
        classE = (FrameLayout) findViewById(R.id.classificationEFrame);
        suited = (ImageView) findViewById(R.id.suited);
        googleMap = (ImageButton) findViewById(R.id.googleMap);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            arrayPos = extras.getInt("arrayPosition");
            cid = extras.getString("cid");
            prevState = extras.getInt("prevState");
            owned = extras.getBoolean("owned");
        }

        if(owned){
            //this is an owned site
            sites = inst.getOwnedSitesMap();
            getSupportActionBar().setTitle("Owned Site");

        } else {
            //this is a known site
            sites = inst.getKnownSitesMap();
            getSupportActionBar().setTitle("Known Site");
        }

        focused = sites.get(arrayPos);
        cid = focused.getCid();
        latitude = focused.getLat();
        longitude = focused.getLon();
        lat.setText("Latitude: "+latitude.toString());
        lon.setText("Longitude: "+longitude.toString());
        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        rating.setRating((focused.getRating()).floatValue());
        ratedBy.setText("Rated By: " + focused.getRatedBy());

        if(owned){

        } else {

            rating.setClickable(true);
            rating.setIsIndicator(false);
            ratingOnStart = rating.getRating();
        }

        StoredData ks = new StoredData();
        images = ks.getImages();
        String id = cid.substring(cid.length()-8);
        int cidEnd = Integer.parseInt(id);
        gallery = images.get(cidEnd);

        //if site has no images
        if(gallery.getHasGallery()){
            imagesList = gallery.getGallery();

            ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, imagesList);
            if(imagesList.size() > 1) {
                imageViews.setPageMargin(-150);
            } else {
                imageViews.setPageMargin(0);
            }
            imageViews.setAdapter(adapter);
            imageViews.setOffscreenPageLimit(imagesList.size()-1);
            hasImages = true;

        } else {
            imageViews.setVisibility(View.GONE);
        }

        List<android.location.Address> address = focused.getAddress();
        thisAddress = address.get(0);

        country.setText(thisAddress.getCountryName() + ", " + thisAddress.getLocality());

        String suitedType = focused.getSuited();
        switch (suitedType){
            case "tent":
                suited.setImageResource(R.drawable.tent01);
                break;

            case "camper":
                suited.setImageResource(R.drawable.campervan01);
                break;

            case "bothy":
                suited.setImageResource(R.drawable.silhouette01);
                break;

            default:
                suited.setVisibility(View.GONE);
                break;
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

        }

        googleMap.setOnClickListener(this);
    }

    @Override
    public void processFinish(String output){

        StoredData inst = new StoredData();

        if(owned){
            //this is an owned site
            sites = inst.getOwnedSitesMap();

        } else {
            //this is a known site
            sites = inst.getKnownSitesMap();
        }

        focused = sites.get(arrayPos);
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

        if(!imagesList.isEmpty()){
            for(int i = 0; i<imagesList.size(); i++){
                //populate the grid view.
                Log.d("View", "Add View for Image");
                imageBit = StringToBitMap(imagesList.get(i));
                ImageButton imageView = new ImageButton(this);
                imageView.setId(i);
                imageView.setImageBitmap(imageBit);
                imageView.setClickable(true);
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
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.googleMap:

                String label = focused.getTitle();
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent i = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(i);

                break;
        }

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(owned) {
            inflater.inflate(R.menu.owned_site_viewer, menu);
        } else {
            inflater.inflate(R.menu.known_site_viewer, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if(owned) {
                    //Intent intent = null;
                    if (prevState == 1) {
                        Intent intent = new Intent(getApplicationContext(), SitesActivity.class);
                        intent.putExtra("fragment", 1);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    updateRating();
                }
                return true;

            case R.id.profile:

                final Intent profile = new Intent(this, ProfileActivity.class);
                profile.putExtra("email", focused.getSiteAdmin());
                profile.putExtra("this_user", false);

                ArrayList<String> users = new ArrayList<>();
                users.add(0, focused.getSiteAdmin());

                new FetchUsers(this, users, showDialog, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        startActivity(profile);
                        finish();
                    }
                }).execute();

                break;

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
                new FetchUsers(this, emails, showDialog, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        startActivity(i);
                    }
                }).execute();

                break;

            case R.id.report:

                new CreateNotification(this, Appconfig.myToken, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        new ReportSite(SiteViewerActivity.this, cid, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {

                                BadgeManager bm = new BadgeManager(SiteViewerActivity.this);
                                bm.checkReportedBadges();
                                Snackbar.make(parentLayout, "This site has been reported to the administrator.", Snackbar.LENGTH_LONG).show();

                            }
                        }).execute();
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
                        new UpdateSite(getApplicationContext(), true, active, cid, null, null, null, null, null, new AsyncResponse() {
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
                intent = new Intent(getApplicationContext(),AddSiteActivity.class);
                //bundle all current details into "add site"
                intent.putExtra("update", true);
                intent.putExtra("arrayPosition", arrayPos);
                intent.putExtra("image", hasImages);
                startActivity(intent);
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {

        if(!owned) {
            updateRating();
        }
    }

    public void updateRating(){
        double ratingOnExit = rating.getRating();

        if(ratingOnExit != ratingOnStart){

            String newRating = Double.toString(ratingOnExit);

            new UpdateSite(this, false, true, cid, null, null, null, newRating, null, new AsyncResponse() {
                @Override
                public void processFinish(String output) {

                    new FetchKnownSites(SiteViewerActivity.this, showDialog, new AsyncResponse() {
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
    }

}
