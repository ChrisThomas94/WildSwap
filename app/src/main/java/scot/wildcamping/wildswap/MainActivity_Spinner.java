package scot.wildcamping.wildswap;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import scot.wildcamping.wildswap.Adapters.CustomSpinnerAdapter;
import scot.wildcamping.wildswap.Adapters.ViewPagerAdapter;
import scot.wildcamping.wildswap.AsyncTask.AsyncResponse;
import scot.wildcamping.wildswap.AsyncTask.FetchKnownSites;
import scot.wildcamping.wildswap.AsyncTask.FetchTradeRequests;
import scot.wildcamping.wildswap.AsyncTask.FetchUnknownSites;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.StoredTrades;
import scot.wildcamping.wildswap.Objects.StoredUsers;
import scot.wildcamping.wildswap.Objects.Trade;
import scot.wildcamping.wildswap.Objects.User;

public class MainActivity_Spinner extends AppCompatActivity {

    private Menu optionsMenu;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    SparseArray<Trade> activeTrades;
    StoredTrades trades;
    String noOfTradesStr;

    String user;
    double latitude;
    double longitude;
    boolean add = false;
    boolean trade = false;
    boolean update = false;
    boolean fetchData = true;
    int fragment = 0;
    boolean isNew;

    private Spinner spinner_nav;
    ArrayList<String> list;

    ViewPager pager;
    ViewPagerAdapter pageAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Owned","Known"};
    int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_spinner);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        user = AppController.getString(this, "user");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        pageAdapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

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
        //tabs.setViewPager(pager);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            add = extras.getBoolean("add");
            fragment = extras.getInt("fragment");
            trade = extras.getBoolean("trade");
            update = extras.getBoolean("update");
            isNew = extras.getBoolean("new");
            fetchData = extras.getBoolean("data");
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

        }

        addItemsToSpinner();
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


        spinner_nav.setAdapter(spinAdapter);

        spinner_nav.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                displayView(position, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        StoredUsers users = new StoredUsers();
        User keyUser = users.getKeyUser();
        System.out.println("email " + keyUser.getEmail());
    }

    private void displayView(final int position, Boolean refresh) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager;
        switch (position) {
            case 0:
                if(isNetworkAvailable() && (fetchData || refresh)) {

                    new FetchKnownSites(this, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            new FetchUnknownSites(MainActivity_Spinner.this, new AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    MapsFragment mapsFragment = new MapsFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.frame_container, mapsFragment).commit();

                                    setTitle(list.get(position));
                                    setRefreshActionButtonState(false);
                                }
                            }).execute();

                        }
                    }).execute();

                } else {
                    MapsFragment mapsFragment = new MapsFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, mapsFragment).commit();

                    setTitle(list.get(position));
                    setRefreshActionButtonState(false);
                }

                break;

            case 1:
                final Intent intent = new Intent(getApplicationContext(), SitesActivity.class);
                intent.putExtra("new", isNew);

                if(isNetworkAvailable()) {
                    StoredData sites = new StoredData();
                    if(sites.getKnownSiteSize() == 0){

                        new FetchKnownSites(this, new AsyncResponse() {
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
                } else {
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                break;

            case 2:
                final Intent i = new Intent(getApplicationContext(), TradesActivity.class);
                i.putExtra("new", isNew);

                if (isNetworkAvailable()) {

                    new FetchTradeRequests(this, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            startActivity(i);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    }).execute();

                } else {
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }

                break;

            case 3:
                final Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                profile.putExtra("this_user", true);
                profile.putExtra("new", isNew);
                overridePendingTransition(0,0);
                startActivity(profile);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
        } else if (id == R.id.action_refresh) {
            setRefreshActionButtonState(true);
            displayView(0, true);
            return true;
        } else if(id == R.id.action_tradeHistory){
            final Intent intent = new Intent(getApplicationContext(), TradeHistoryActivity.class);

            new FetchTradeRequests(this, new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    startActivity(intent);
                }
            }).execute();
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