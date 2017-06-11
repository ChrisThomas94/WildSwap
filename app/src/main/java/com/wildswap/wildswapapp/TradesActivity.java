package com.wildswap.wildswapapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.PointTarget;

import java.util.ArrayList;

import com.wildswap.wildswapapp.Adapters.CustomSpinnerAdapter;
import com.wildswap.wildswapapp.Adapters.ViewPagerAdapter;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchKnownSites;
import com.wildswap.wildswapapp.AsyncTask.FetchTradeRequests;

public class TradesActivity extends AppCompatActivity implements OnShowcaseEventListener {

    // Declaring Your View and Variables

    private Menu optionsMenu;
    private Spinner spinner_nav;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Received","Sent"};
    //CharSequence Titles[] =
    int Numboftabs =2;
    ArrayList<String> list;
    int currPosition;
    boolean initialSelection = false;
    String user;
    boolean register = false;
    boolean tradesTutorial = false;
    boolean sitesTutorial;
    boolean showDialog = true;
    View parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);

        user = AppController.getString(this, "user");

        parentLayout = findViewById(android.R.id.content);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            register = extras.getBoolean("new");
            sitesTutorial = extras.getBoolean("sitesTutorial");
            tradesTutorial = extras.getBoolean("tradesTutorial");

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

        if(register && !tradesTutorial){
            showcase();
        }

        addItemsToSpinner();
    }

    public void showcase(){
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // This aligns button to the bottom left side of screen
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.setMargins(90,0,0,90);

        pager.setVisibility(View.INVISIBLE);

        //ViewTarget t = new ViewTarget();
        PointTarget t = new PointTarget(280, 300);

        ShowcaseView sv = new ShowcaseView.Builder(this)
                .setTarget(t)
                .setContentTitle("Active Trades")
                .setContentText("Trade requests that you have received from other users will be listed in this tab.")
                .blockAllTouches()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShowcaseEventListener(this)
                .build();

        sv.setButtonPosition(lps);

    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // This aligns button to the bottom left side of screen
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.setMargins(90,0,0,90);

        showcaseView = new ShowcaseView.Builder(this)
                .setTarget( new PointTarget(810, 300))
                .setContentTitle("Trade History")
                .setContentText("Trade requests that you have sent to other users will be listed in this tab.")
                .blockAllTouches()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        pager.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                    }
                })
                .build();
        showcaseView.setButtonPosition(lps);
        tradesTutorial = true;
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        list = new ArrayList<String>();
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
        spinner_nav.setSelection(2);
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
                // TODO Auto-generated method stub

            }
        });
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        FragmentActivity fragmentActivity = null;
        Fragment fragment = null;
        MapsFragment mapsFragment = null;
        switch (position) {
            case 0:
                Intent i = new Intent(getApplicationContext(), MainActivity_Spinner.class);
                i.putExtra("data", false);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;

            case 1:
                final Intent intent = new Intent(getApplicationContext(), SitesActivity.class);
                intent.putExtra("new", register);
                intent.putExtra("sitesTutorial", sitesTutorial);
                intent.putExtra("tradesTutorial", tradesTutorial);

                if(isNetworkAvailable()){
                    new FetchKnownSites(this, showDialog, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    }).execute();
                } else {
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }

                break;

            case 2:
                if(isNetworkAvailable()) {
                    setRefreshActionButtonState(true);
                    showDialog = false;

                    final Intent in = new Intent(getApplicationContext(), TradesActivity.class);

                    new FetchTradeRequests(this, showDialog, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            setRefreshActionButtonState(false);
                            startActivity(in);
                            overridePendingTransition(0,0);
                            finish();
                        }
                    }).execute();
                } else {
                    Snackbar.make(parentLayout, "No connection", Snackbar.LENGTH_LONG).show();
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
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.trades_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            displayView(2);

        } else if(id == R.id.action_tradeHistory){
            Intent intent = new Intent(getApplicationContext(), TradeHistoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings1) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
}