package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import scot.wildcamping.wildscotland.Adapters.TradeOwnedSitesAdapter;
import scot.wildcamping.wildscotland.Adapters.TradeUnknownSitesAdapter;
import scot.wildcamping.wildscotland.AsyncTask.AsyncResponse;
import scot.wildcamping.wildscotland.AsyncTask.CreateNotification;
import scot.wildcamping.wildscotland.AsyncTask.CreateTrade;
import scot.wildcamping.wildscotland.AsyncTask.FetchQuestions;
import scot.wildcamping.wildscotland.AsyncTask.FetchSiteImages;
import scot.wildcamping.wildscotland.AsyncTask.FetchUsers;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.Objects.StoredData;

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

    ViewPager unknownPage;
    ViewPager ownedPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_simple);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

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

        new FetchUsers(this, emails, new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //set the adapters off populating
                dealers = inst.getDealers();
                System.out.println(dealers);

                ownedSites = new TradeOwnedSitesAdapter(getApplicationContext(), ownedMap);
                ownedPage.setAdapter(ownedSites);
                ownedPage.setClipToPadding(false);
                ownedPage.setPadding(100, 0, 100, 0);
                ownedPage.setPageMargin(30);

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
            case R.id.yourSite:

                final Intent i = new Intent(this, OwnedSiteViewerActivity.class);
                i.putExtra("cid", send_cid);
                i.putExtra("prevState", 2);

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
        String token = selectedUnknownSites.get(currentSite).getToken();
        String recieve_cid = selectedUnknownSites.get(currentSite).getCid();
        LatLng position = selectedUnknownSites.get(currentSite).getPosition();

        String send_cid = ownedMap.get(currentOwnedSite).getCid();

        switch (menuItem.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            case R.id.action_contact:

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{siteAdmin});
                i.putExtra(Intent.EXTRA_SUBJECT, "Wild Scotland - Trade");
                i.putExtra(Intent.EXTRA_TEXT, "Hello fellow wild camper, I am contacting you because...");

                startActivity(i);
                break;

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

                if(isNetworkAvailable()) {
                    new CreateTrade(this, send_cid, recieve_cid).execute();
                    new CreateNotification(this, token).execute();
                }

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                intent.putExtra("trade", true);
                intent.putExtra("latitude", position.latitude);
                intent.putExtra("longitude", position.longitude);
                startActivity(intent);
                finish();
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
