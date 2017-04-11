package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.Adapters.TradeOwnedSitesAdapter;
import scot.wildcamping.wildscotland.Adapters.TradeUnknownSitesAdapter;
import scot.wildcamping.wildscotland.AsyncTask.AsyncResponse;
import scot.wildcamping.wildscotland.AsyncTask.CreateNotification;
import scot.wildcamping.wildscotland.AsyncTask.CreateTrade;
import scot.wildcamping.wildscotland.AsyncTask.FetchQuestions;
import scot.wildcamping.wildscotland.AsyncTask.FetchSiteImages;
import scot.wildcamping.wildscotland.AsyncTask.FetchSomeUsers;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.Objects.knownSite;

/**
 * Created by Chris on 11-Mar-16.
 *
 */
public class TradeActivitySimple extends AppCompatActivity implements View.OnClickListener {

    PagerAdapter ownedSites;
    PagerAdapter unknownSites;

    ArrayList<LatLng> cluster = null;
    knownSite inst = new knownSite();
    int recieveSize;
    int ownedSitesSize;
    int unknownSitesSize;
    SparseArray<Site> selectedUnknownSites = new SparseArray<>();
    SparseArray<Site> ownedMap;
    SparseArray<Site> unknownMap;
    SparseArray<User> dealers;
    Site recieveSite;
    Site sendSite;
    String send_cid;
    String recieve_cid;
    String recieve_token;
    int ownedSiteInit =0;
    int prevSite = 0;
    int nextSite = 0;
    String trade = "trade";

    TextView recieveTitle;
    TextView placeholderFeatures;
    RatingBar recieveRating;

    TextView sendTitle;
    TextView placeholderFeaturesYours;
    RatingBar sendRating;
    RelativeLayout yourSite;

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

        recieveTitle = (TextView)findViewById(R.id.recieveTitle);
        placeholderFeatures = (TextView)findViewById(R.id.placeholderFeatures);
        recieveRating = (RatingBar)findViewById(R.id.recieveRating);

        sendTitle = (TextView)findViewById(R.id.sendTitle);
        placeholderFeaturesYours = (TextView)findViewById(R.id.placeholderFeaturesYours);
        sendRating = (RatingBar)findViewById(R.id.sendRating);

        yourSite = (RelativeLayout)findViewById(R.id.yourSite);


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

        new FetchSomeUsers(this, emails, new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //set the adapters off populating
                dealers = inst.getDealers();

            }
        }).execute();

        ownedSites = new TradeOwnedSitesAdapter(this, ownedMap);
        unknownSites = new TradeUnknownSitesAdapter(this, unknownMap, dealers);

        //initialise unknown site
        //genUnknownSite(nextSite);

        //initialise known site
        //genOwnedSite(ownedSiteInit);

    }

    public void genOwnedSite(int random){
        sendSite = ownedMap.get(random);

        send_cid = sendSite.getCid();

        sendTitle.setText(sendSite.getTitle());

        if (sendSite.getFeature1().equals("0") && sendSite.getFeature2().equals("0") && sendSite.getFeature3().equals("0") && sendSite.getFeature4().equals("0") && sendSite.getFeature5().equals("0") && sendSite.getFeature6().equals("0") && sendSite.getFeature7().equals("0") && sendSite.getFeature8().equals("0") && sendSite.getFeature9().equals("0") && sendSite.getFeature10().equals("0")){
            placeholderFeaturesYours.setVisibility(View.VISIBLE);
        } else {
            placeholderFeaturesYours.setVisibility(View.GONE);
        }

        sendRating.setRating((sendSite.getRating()).floatValue());
    }

    public void genUnknownSite(int random){

        recieveSite = selectedUnknownSites.get(random);

        recieve_cid = recieveSite.getCid();

        recieve_token = recieveSite.getToken();

        recieveTitle.setText(recieveSite.getTitle());

        if (recieveSite.getFeature1().equals("0") && recieveSite.getFeature2().equals("0") && recieveSite.getFeature3().equals("0") && recieveSite.getFeature4().equals("0") && recieveSite.getFeature5().equals("0") && recieveSite.getFeature6().equals("0") && recieveSite.getFeature7().equals("0") && recieveSite.getFeature8().equals("0") && recieveSite.getFeature9().equals("0") && recieveSite.getFeature10().equals("0")){
            placeholderFeatures.setVisibility(View.VISIBLE);
        } else {
            placeholderFeatures.setVisibility(View.GONE);
        }

        recieveRating.setRating((recieveSite.getRating()).floatValue());
    }

    @Override
    public void onClick(View v){

        Intent intent;
        switch (v.getId())
        {
            case R.id.right_arrow:

                if(ownedSiteInit == ownedSitesSize-1){
                    ownedSiteInit = -1;
                }
                ownedSiteInit++;
                genOwnedSite(ownedSiteInit);

                break;

            case R.id.left_arrow:

                if(ownedSiteInit == 0){
                    ownedSiteInit = ownedSitesSize;
                }
                ownedSiteInit--;
                genOwnedSite(ownedSiteInit);


                break;

            case R.id.refresh_trade:

                Random ran = new Random();
                if(nextSite == selectedUnknownSites.size()-1){
                    nextSite = 0;
                } else {
                    nextSite++;
                }
                //nextSite = getRandomWithExclusion(ran, 0, selectedUnknownSites.unknownSiteSize()-1, nextSite);
                genUnknownSite(nextSite);
                break;

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
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                //Intent intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                finish();
                return true;

            case R.id.action_contact:
                //Intent i = new Intent(getApplicationContext(), ContactUser.class);
                //i.putExtra("contact", recieveSite.getSiteAdmin());
                //intent.putExtra("date", date); instance of date
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{recieveSite.getSiteAdmin()});
                i.putExtra(Intent.EXTRA_SUBJECT, "Wild Scotland - Trade");
                i.putExtra(Intent.EXTRA_TEXT, "Hello fellow wild camper, I am contacting you because...");

                startActivity(i);
                break;

            case R.id.profile:
                //open that user's profile
                if(isNetworkAvailable()) {
                    try {
                        String questions = new FetchQuestions(this, recieveSite.getSiteAdmin()).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
                in.putExtra("email", recieveSite.getSiteAdmin());
                in.putExtra("this_user", false);
                //intent.putExtra("date", date); instance of date
                startActivity(in);
                break;

            case R.id.action_submit:
                new CreateTrade(this, send_cid, recieve_cid).execute();
                new CreateNotification(this, recieve_token).execute();

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                intent.putExtra("trade", true);
                intent.putExtra("latitude", recieveSite.getPosition().latitude);
                intent.putExtra("longitude", recieveSite.getPosition().longitude);
                startActivity(intent);
                finish();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
