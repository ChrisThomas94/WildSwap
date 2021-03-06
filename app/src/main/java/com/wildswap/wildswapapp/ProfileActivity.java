package com.wildswap.wildswapapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import com.wildswap.wildswapapp.Adapters.CustomSpinnerAdapter;
import com.wildswap.wildswapapp.Adapters.ViewPagerAdapter;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchTradeRequests;
import com.wildswap.wildswapapp.AsyncTask.FetchUsers;
import com.wildswap.wildswapapp.AsyncTask.SubmitVouch;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.StoredTrades;
import com.wildswap.wildswapapp.Objects.User;
import static android.view.View.GONE;
import static com.wildswap.wildswapapp.R.id.vouch;

/**
 *
 * Created by Chris on 08-Apr-16.
 */
public class ProfileActivity extends AppCompatActivity {

    private Spinner spinner_nav;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Questions", "Badges"};
    int Numboftabs =2;
    ArrayList<String> list;
    boolean initialSelection = false;
    String user;
    String otherEmail;
    int progressValue = 0;
    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();
    SparseArray<User> dealers = inst.getDealers();
    StoredTrades trades = new StoredTrades();
    Boolean showDialog = true;
    int vouchNum;
    Boolean complete = false;

    User otherUser;


    TextView txtName;
    TextView txtEmail;
    TextView txtBio;
    TextView txtWhy;
    Boolean this_user = false;
    CircleImageView profile_pic;
    ImageView cover_pic;
    ProgressBar progress;
    TextView progressText;
    TextView userType;
    RelativeLayout progressLayout;
    TextView numSitesText;
    TextView numTradesText;
    TextView numVouches;
    RelativeLayout vouchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            this_user = extras.getBoolean("this_user");
            otherEmail = extras.getString("email");
            complete = extras.getBoolean("quizComplete");
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
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

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtWhy = (TextView) findViewById(R.id.why);
        txtBio = (TextView) findViewById(R.id.bio);
        profile_pic = (CircleImageView) findViewById(R.id.profilePicture);
        cover_pic = (ImageView) findViewById(R.id.backgroundImage);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        userType = (TextView) findViewById(R.id.profileStatusText);
        numSitesText = (TextView) findViewById(R.id.numSites);
        numTradesText = (TextView) findViewById(R.id.numTrades);
        numVouches = (TextView) findViewById(R.id.vouchNumber);
        vouchButton = (RelativeLayout) findViewById(R.id.vouchLayout);

        int ownedSites = inst.getOwnedSiteSize();
        //int completedTrades = trades.getAcceptedTradesSize();
        int completedTrades = 0;

        for(int i = 0; i<trades.getInactiveTradesSize(); i++){
            if(trades.getInactiveTrades().get(i).getStatus() == 2){
                completedTrades++;
            }
        }

        if(this_user){

            txtName.setText(thisUser.getName());
            txtEmail.setText(thisUser.getEmail());
            String bio = thisUser.getBio();
            String why = thisUser.getWhy();
            String profile_pic = thisUser.getProfile_pic();
            String cover_pic = thisUser.getCover_pic();
            String type = thisUser.getUserType();
            userType.setText(type);
            vouchNum = thisUser.getNumVouch();

            numSitesText.setText(String.valueOf(ownedSites));
            numTradesText.setText(String.valueOf(completedTrades));

            if(!type.equals("") && !type.isEmpty()) {
                updateProgress();
                System.out.println("update type" + type);
            }

            if(bio.equals("null")){
                txtBio.setText("");
            } else {
                txtBio.setText(bio);
                updateProgress();
                System.out.println("update bio");
            }

            if(why.equals("null")){
                txtWhy.setText("");
            } else {
                txtWhy.setText(why);
                updateProgress();
                System.out.println("update why");
            }

            if(profile_pic != null && !profile_pic.equals("null") && !profile_pic.equals("")){
                Bitmap bit = StringToBitMap(profile_pic);
                this.profile_pic.setImageBitmap(bit);
                updateProgress();

                System.out.println("update profile pic");

            }

            if(cover_pic != null && !cover_pic.equals("null") && !cover_pic.equals("")){
                Bitmap bit = StringToBitMap(cover_pic);
                this.cover_pic.setImageBitmap(bit);
                updateProgress();
                System.out.println("update cover pic");
            }

            numVouches.setText(String.valueOf(vouchNum));

            if(progressValue == 100){
                progressLayout.setVisibility(GONE);
            }

            BadgeManager bM = new BadgeManager(this);
            //bM.checkTradeBadges();
            //bM.checkReportedBadges();
            //bM.checkGiftedBadges();
            //bM.checkCountryBadges();
            //bM.checkSiteBadges();

            if(complete){
                bM.awardQuizBadge();
            }

            vouchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "You cannot vouch for yourself! Try vouching for other users if you believe they are trustworthy.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            for(int i = 0; i<dealers.size(); i++){
                System.out.println("in loop" + dealers.get(i).getEmail());
                if(dealers.get(i).getEmail().equals(otherEmail)){
                    otherUser = dealers.get(i);
                    inst.setOtherUser(otherUser);
                    System.out.println("dealer email: "+dealers.get(i).getEmail());
                    break;
                }
            }

            if(otherUser != null) {

                txtName.setText(otherUser.getName());
                txtEmail.setText(otherUser.getEmail());
                numTradesText.setText(String.valueOf(otherUser.getNumTrades()));
                numSitesText.setText(String.valueOf(otherUser.getNumSites()));
                vouchNum = otherUser.getNumVouch();

                String type = otherUser.getUserType();
                userType.setText(type);

                String bio = otherUser.getBio();
                String why = otherUser.getWhy();

                if (bio.equals("null")) {
                    txtBio.setText("");
                } else {
                    txtBio.setText(bio);
                }

                if (why.equals("null")) {
                    txtWhy.setText("");
                } else {
                    txtWhy.setText(why);
                    updateProgress();
                    System.out.println("update why");
                }

                String image = otherUser.getProfile_pic();
                String cover = otherUser.getCover_pic();

                if (!image.equals("null") || !image.equals("")) {
                    Bitmap bit = StringToBitMap(image);
                    profile_pic.setImageBitmap(bit);
                    updateProgress();
                }

                if (!cover.equals("null") || !cover.equals("")) {
                    Bitmap bit = StringToBitMap(cover);
                    cover_pic.setImageBitmap(bit);
                    updateProgress();
                }

                numVouches.setText(String.valueOf(vouchNum));

                progress.setVisibility(GONE);
                progressLayout.setVisibility(GONE);

                vouchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vouchNum++;
                        numVouches.setText(String.valueOf(vouchNum));
                        Toast.makeText(getApplicationContext(), "You have vouched for this user!", Toast.LENGTH_SHORT).show();
                        vouch();
                    }
                });
            }
        }

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateProfileActivity.class);
                intent.putExtra("update", true);
                startActivity(intent);
            }
        });

        addItemsToSpinner();
    }

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        list = new ArrayList<>();
        list.add("Map");
        list.add("Sites");
        list.add("Trades");
        list.add("Profile");

        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);



        // Default ArrayAdapter with default spinner item layout, getting some
        // view rendering problem in lollypop device, need to test in other
        // devices

		/*
		 * ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_spinner_item, list);
		 * spinAdapter.setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item);
		 */

        spinner_nav.setAdapter(spinAdapter);
        spinner_nav.setSelection(3);
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                if(initialSelection){
                    displayView(position);
                }
                initialSelection = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("data", false);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;
            case 1:
                Intent intent = new Intent(getApplicationContext(), SitesActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;

            case 2:
                final Intent trade = new Intent(getApplicationContext(), TradesActivity.class);

                if (isNetworkAvailable()) {

                    new FetchTradeRequests(this, showDialog, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            startActivity(trade);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    }).execute();

                } else {
                    startActivity(trade);
                    overridePendingTransition(0, 0);
                    finish();
                }

                break;

            case 3:
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                profile.putExtra("this_user", true);
                startActivity(profile);
                overridePendingTransition(0,0);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(this_user){
            getMenuInflater().inflate(R.menu.profile, menu);
        } else {
            getMenuInflater().inflate(R.menu.other_user_profile, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings1) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_about){
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_refresh) {
            if(isNetworkAvailable()) {

                ArrayList<String> users = new ArrayList<>();
                users.add(0, thisUser.getEmail());

                new FetchUsers(this, users, showDialog, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        displayView(3);
                    }
                }).execute();

            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "No network connection!", Snackbar.LENGTH_LONG)
                        .show();
            }
        } else if(id == R.id.updateProfile){
            Intent intent = new Intent(getBaseContext(), CreateProfileActivity.class);
            intent.putExtra("update", true);
            startActivity(intent);
            return true;
        } else if(id == vouch){
            vouch();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void vouch(){
        if(isNetworkAvailable()) {
            new SubmitVouch(this, otherEmail, new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    vouchNum++;
                    numVouches.setText(String.valueOf(vouchNum));
                    BadgeManager bm = new BadgeManager(ProfileActivity.this);
                    bm.awardVouchBadge();
                }
            }).execute();
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "No network connection!", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void updateProgress(){

        progressValue = progress.getProgress()+20;
        progress.setProgress(progressValue);
        progressText.setText(progressValue+"% Complete");
    }
}
