package scot.wildcamping.wildscotland;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import scot.wildcamping.wildscotland.adapter.CustomSpinnerAdapter;
import scot.wildcamping.wildscotland.adapter.NavDrawerListAdapter;
import scot.wildcamping.wildscotland.adapter.ViewPagerAdapter;
import scot.wildcamping.wildscotland.model.NavDrawerItem;
import scot.wildcamping.wildscotland.model.StoredTrades;
import scot.wildcamping.wildscotland.model.Trade;

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
    int fragment = 0;
    int currPosition;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

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

        }

        //mTitle = mDrawerTitle = getTitle();

        trades = new StoredTrades();
        activeTrades = new SparseArray<>();
        activeTrades = trades.getActiveTrades();

        noOfTradesStr = Integer.toString(activeTrades.size());

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //getSupportActionBar().setLogo(getDrawable(logo));
            getSupportActionBar().setDisplayUseLogoEnabled(true);

        }
        addItemsToSpinner();
    }


    // add items into spinner dynamically
    public void addItemsToSpinner() {

        list = new ArrayList<String>();
        list.add("Map");
        list.add("Sites");
        //list.add("Known Sites");
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

        spinner_nav.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                displayView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
                mapsFragment = new MapsFragment();
                if(isNetworkAvailable()) {
                    try {
                        String known_result = new FetchKnownSites(this).execute().get();
                        String unknown_result = new FetchUnknownSites(this).execute().get();

                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                break;
            case 1:
                if(isNetworkAvailable()) {
                    try {
                        String known_result = new FetchKnownSites(this).execute().get();
                        String unknown_result = new FetchUnknownSites(this).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent intent = new Intent(getApplicationContext(), Sites.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;

            case 2:
                if(isNetworkAvailable()) {
                    try {
                        String trades_result = new FetchTradeRequests(this).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent i = new Intent(getApplicationContext(), Trades.class);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
                break;

            case 3:
                if(isNetworkAvailable()) {
                    try {
                        String questions = new FetchQuestions(this, AppController.getString(this, "email")).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                profile.putExtra("this_user", true);
                startActivity(profile);
                overridePendingTransition(0,0);
                finish();
                break;

            default:
                break;
        }

        if(mapsFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, mapsFragment).commit();

            setTitle(list.get(position));
            setRefreshActionButtonState(false);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.main, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings1) {
            Intent intent = new Intent(getApplicationContext(), SettingsFragment.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_search1) {
            Toast.makeText(getApplicationContext(), "Search Clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_add1) {
            Toast.makeText(getApplicationContext(), "Add Clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_delete1) {
            Toast.makeText(getApplicationContext(), "Delete Clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_refresh) {
            setRefreshActionButtonState(true);
            displayView(0);
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