package com.wildswap.wildswapapp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.wildswap.wildswapapp.Adapters.TradeOwnedSitesAdapter;
import com.wildswap.wildswapapp.Adapters.TradeUnknownSitesAdapter;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.CreateNotification;
import com.wildswap.wildswapapp.AsyncTask.CreateTrade;
import com.wildswap.wildswapapp.AsyncTask.FetchSiteImages;
import com.wildswap.wildswapapp.AsyncTask.FetchUsers;
import com.wildswap.wildswapapp.Objects.Site;
import com.wildswap.wildswapapp.Objects.User;
import com.wildswap.wildswapapp.Objects.StoredData;

/**
 * Created by Chris on 11-Mar-16.
 *
 */
public class TradeActivitySimple extends AppCompatActivity implements View.OnClickListener {

    PagerAdapter ownedSites;
    PagerAdapter unknownSites;

    ArrayList<LatLng> cluster = null;
    StoredData inst = new StoredData();
    int recieveSize;
    int ownedSitesSize;
    int unknownSitesSize;
    SparseArray<Site> selectedUnknownSites = new SparseArray<>();
    SparseArray<Site> ownedMap;
    SparseArray<Site> unknownMap;
    SparseArray<User> dealers;
    String send_cid;
    String trade = "trade";
    boolean showDialog = true;

    ViewPager unknownPage;
    ViewPager ownedPage;

    Geocoder geocoder;
    View parentLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_simple);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        parentLayout = findViewById(android.R.id.content);

        geocoder = new Geocoder(this, Locale.getDefault());

        ownedMap = inst.getOwnedSitesMap();
        unknownMap = inst.getUnknownSitesMap();
        unknownSitesSize = inst.getUnknownSitesSize();
        ownedSitesSize = inst.getOwnedSiteSize();

        unknownPage = (ViewPager)findViewById(R.id.unknownSiteViewPager);
        ownedPage = (ViewPager)findViewById(R.id.ownedSiteViewPager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cluster = extras.getParcelableArrayList("cluster");
            recieveSize = cluster.size();
            System.out.println("Trade" + cluster + recieveSize);
        }

        ArrayList<String> emails = new ArrayList<>();

        //where lat lng of cluster = lat lng of unknownSitesMap
        //add to new unknownSitesMap
        for(int i=0; i<recieveSize; i++){

            for(int j=0; j<unknownSitesSize; j++){
                Site currentSite = unknownMap.get(j);

                if (cluster.get(i).equals(currentSite.getPosition())) {
                    emails.add(currentSite.getSiteAdmin());
                    System.out.println(currentSite.getTitle());
                    selectedUnknownSites.put(i, currentSite);
                }
            }
        }

        new FetchUsers(this, emails, showDialog, new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //set the adapters off populating
                dealers = inst.getDealers();
                System.out.println(dealers);

                ownedSites = new TradeOwnedSitesAdapter(getApplicationContext(), ownedMap);
                ownedPage.setAdapter(ownedSites);
                ownedPage.setClipToPadding(false);

                if(ownedMap.size() > 1){
                    ownedPage.setPadding(100, 0, 100, 0);
                    ownedPage.setPageMargin(30);
                }

                if(ownedMap.size()%2 == 1){
                    ownedPage.setCurrentItem((ownedMap.size()/2));
                } else {
                    ownedPage.setCurrentItem(ownedMap.size()/2);
                }

                System.out.println("selected unknown sites "+selectedUnknownSites.size());

                System.out.println("dealers "+dealers.size());

                unknownSites = new TradeUnknownSitesAdapter(getApplicationContext(), selectedUnknownSites, dealers);
                unknownPage.setAdapter(unknownSites);

            }
        }).execute();
    }


    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.ownedSiteLinear:

                final Intent i = new Intent(this, SiteViewerActivity.class);
                i.putExtra("cid", send_cid);
                i.putExtra("prevState", 2);
                i.putExtra("owned", true);

                new FetchSiteImages(this, send_cid, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        startActivity(i);
                    }
                }).execute();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int currentSite = unknownPage.getCurrentItem();
        int currentOwnedSite = ownedPage.getCurrentItem();

        String siteAdmin = selectedUnknownSites.get(currentSite).getSiteAdmin();
        final String token = selectedUnknownSites.get(currentSite).getToken();
        String recieve_cid = selectedUnknownSites.get(currentSite).getCid();
        LatLng position = selectedUnknownSites.get(currentSite).getPosition();
        String send_cid = ownedMap.get(currentOwnedSite).getCid();

        List<Address> address;
        String saveAddress = "Address unobtainable";

        try {
            address = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if(address.get(0).getLocality() == null || address.get(0).getLocality().equals("null")){
                saveAddress = address.get(0).getCountryName();
            } else {
                saveAddress = address.get(0).getCountryName() + ", " + address.get(0).getLocality();
            }

        } catch (IOException e) {

        }


        switch (menuItem.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            case R.id.profile:

                //open that user's profile
                if(isNetworkAvailable()) {
                    //new FetchQuestions(this, siteAdmin).execute();
                }

                Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
                in.putExtra("email", siteAdmin);
                in.putExtra("this_user", false);
                startActivity(in);
                break;

            case R.id.action_submit:

                final Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                intent.putExtra("trade", true);
                intent.putExtra("latitude", position.latitude);
                intent.putExtra("longitude", position.longitude);

                if(isNetworkAvailable()) {
                    new CreateTrade(this, send_cid, recieve_cid, saveAddress, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {

                            Toast.makeText(TradeActivitySimple.this, output, Toast.LENGTH_LONG).show();

                            if(output.equals("Trade Sent!")){
                                System.out.println("Create Trade Notification");
                                new CreateNotification(TradeActivitySimple.this, token, new AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        startActivity(intent);
                                        finish();
                                    }
                                }).execute();
                            }
                        }
                    }).execute();
                } else {
                    Snackbar.make(parentLayout, "No Internet Connection", Toast.LENGTH_LONG).show();

                }

                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
