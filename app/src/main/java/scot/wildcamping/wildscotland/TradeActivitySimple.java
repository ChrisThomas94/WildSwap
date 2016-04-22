package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

/**
 * Created by Chris on 11-Mar-16.
 */
public class TradeActivitySimple extends AppCompatActivity implements View.OnClickListener {

    ArrayList<LatLng> cluster = null;
    knownSite inst = new knownSite();
    int recieveSize;
    int ownedSitesSize;
    int unknownSitesSize;
    SparseArray<Site> selectedUnknownSites = new SparseArray<>();
    SparseArray<Site> unknownSitesSorted = new SparseArray<>();     //sorted based on popularity
    SparseArray<Site> ownedMap;
    SparseArray<Site> unknownMap;
    Site recieveSite;
    Site sendSite;
    String send_cid;
    String recieve_cid;
    int ownedSiteInit =0;
    int prevSite = 0;
    int nextSite = 0;

    TextView recieveTitle;
    TextView placeholderFeatures;
    ImageView features1;
    ImageView features2;
    ImageView features3;
    ImageView features4;
    ImageView features5;
    ImageView features6;
    ImageView features7;
    ImageView features8;
    ImageView features9;
    ImageView features10;
    RatingBar recieveRating;

    TextView sendTitle;
    TextView placeholderFeaturesYours;
    ImageView sendFeatures1;
    ImageView sendFeatures2;
    ImageView sendFeatures3;
    ImageView sendFeatures4;
    ImageView sendFeatures5;
    ImageView sendFeatures6;
    ImageView sendFeatures7;
    ImageView sendFeatures8;
    ImageView sendFeatures9;
    ImageView sendFeatures10;
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

        //Button cancel = (Button)findViewById(R.id.btnCancel_Trade);
        //Button refresh = (Button)findViewById(R.id.btnRefresh_Trade);
        //Button send = (Button)findViewById(R.id.btnSend_Trade);

        //Button right = (Button)findViewById(R.id.btn_right);
        //Button left = (Button)findViewById(R.id.btn_left);

        ImageView rightArrow = (ImageView)findViewById(R.id.right_arrow);
        ImageView leftArrow = (ImageView)findViewById(R.id.left_arrow);
        ImageView refreshTrade = (ImageView)findViewById(R.id.refresh_trade);

        recieveTitle = (TextView)findViewById(R.id.recieveTitle);
        placeholderFeatures = (TextView)findViewById(R.id.placeholderFeatures);
        features1 = (ImageView)findViewById(R.id.features1);
        features2 = (ImageView)findViewById(R.id.features2);
        features3 = (ImageView)findViewById(R.id.features3);
        features4 = (ImageView)findViewById(R.id.features4);
        features5 = (ImageView)findViewById(R.id.features5);
        features6 = (ImageView)findViewById(R.id.features6);
        features7 = (ImageView)findViewById(R.id.features7);
        features8 = (ImageView)findViewById(R.id.features8);
        features9 = (ImageView)findViewById(R.id.features9);
        features10 = (ImageView)findViewById(R.id.features10);
        recieveRating = (RatingBar)findViewById(R.id.recieveRating);

        sendTitle = (TextView)findViewById(R.id.sendTitle);
        placeholderFeaturesYours = (TextView)findViewById(R.id.placeholderFeaturesYours);
        sendFeatures1 = (ImageView)findViewById(R.id.sendFeatures1);
        sendFeatures2 = (ImageView)findViewById(R.id.sendFeatures2);
        sendFeatures3 = (ImageView)findViewById(R.id.sendFeatures3);
        sendFeatures4 = (ImageView)findViewById(R.id.sendFeatures4);
        sendFeatures5 = (ImageView)findViewById(R.id.sendFeatures5);
        sendFeatures6 = (ImageView)findViewById(R.id.sendFeatures6);
        sendFeatures7 = (ImageView)findViewById(R.id.sendFeatures7);
        sendFeatures8 = (ImageView)findViewById(R.id.sendFeatures8);
        sendFeatures9 = (ImageView)findViewById(R.id.sendFeatures9);
        sendFeatures10 = (ImageView)findViewById(R.id.sendFeatures10);
        sendRating = (RatingBar)findViewById(R.id.sendRating);

        yourSite = (RelativeLayout)findViewById(R.id.yourSite);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cluster = extras.getParcelableArrayList("cluster");
            recieveSize = cluster.size();
            System.out.println("Trade" + cluster + recieveSize);
        }

        if(recieveSize == 1){
            //refresh.setVisibility(View.INVISIBLE);
            refreshTrade.setVisibility(View.INVISIBLE);
        }

        if(ownedSitesSize == 1){
            rightArrow.setVisibility(View.INVISIBLE);
            leftArrow.setVisibility(View.INVISIBLE);
        }

        //where lat lng of cluster = lat lng of map
        //add to new map

        for(int i=0; i<recieveSize; i++){

            for(int j=0; j<unknownSitesSize; j++){
                Site currentSite = unknownMap.get(j);

                if (cluster.get(i).equals(currentSite.getPosition())) {
                    System.out.println(currentSite.getTitle());
                    selectedUnknownSites.put(i, currentSite);
                    unknownSitesSorted.put(i, currentSite);
                }
            }
        }

        //initialise unknown site
        genUnknownSite(nextSite);

        //initialise known site
        genOwnedSite(ownedSiteInit);

        //cancel.setOnClickListener(this);
        //refresh.setOnClickListener(this);
        //send.setOnClickListener(this);
        //right.setOnClickListener(this);
        //left.setOnClickListener(this);
        rightArrow.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        refreshTrade.setOnClickListener(this);
        //yourSite.setOnClickListener(this);
    }

    public void genOwnedSite(int random){
        sendSite = ownedMap.get(random);

        send_cid = sendSite.getCid();

        sendTitle.setText(sendSite.getTitle());

        if(sendSite.getFeature1().equals("0")){
            sendFeatures1.setVisibility(View.GONE);
        }else{
            sendFeatures1.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature2().equals("0")){
            sendFeatures2.setVisibility(View.GONE);
        }else{
            sendFeatures2.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature3().equals("0")){
            sendFeatures3.setVisibility(View.GONE);
        }else{
            sendFeatures3.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature4().equals("0")){
            sendFeatures4.setVisibility(View.GONE);
        }else{
            sendFeatures4.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature5().equals("0")){
            sendFeatures5.setVisibility(View.GONE);
        }else{
            sendFeatures5.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature6().equals("0")){
            sendFeatures6.setVisibility(View.GONE);
        }else{
            sendFeatures6.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature7().equals("0")){
            sendFeatures7.setVisibility(View.GONE);
        }else{
            sendFeatures7.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature8().equals("0")){
            sendFeatures8.setVisibility(View.GONE);
        }else{
            sendFeatures8.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature9().equals("0")){
            sendFeatures9.setVisibility(View.GONE);
        }else{
            sendFeatures9.setVisibility(View.VISIBLE);
        }
        if(sendSite.getFeature10().equals("0")){
            sendFeatures10.setVisibility(View.GONE);
        }else{
            sendFeatures10.setVisibility(View.VISIBLE);
        }

        if (sendSite.getFeature1().equals("0") && sendSite.getFeature2().equals("0") && sendSite.getFeature3().equals("0") && sendSite.getFeature4().equals("0") && sendSite.getFeature5().equals("0") && sendSite.getFeature6().equals("0") && sendSite.getFeature7().equals("0") && sendSite.getFeature8().equals("0") && sendSite.getFeature9().equals("0") && sendSite.getFeature10().equals("0")){
            placeholderFeaturesYours.setVisibility(View.VISIBLE);
        } else {
            placeholderFeaturesYours.setVisibility(View.GONE);
        }

        sendRating.setRating((sendSite.getRating()).floatValue());
    }

    public void genUnknownSite(int random){

        /*int currentPop;
        int leastPopularSite = 0;
        int leastPop = 100;

        if(unknownSitesSorted.size() == 0){
            unknownSitesSorted = selectedUnknownSites.clone();
            System.out.println("i am clonning");
        }

        for(int i = 0; i < unknownSitesSorted.size(); i++){

            currentPop = unknownSitesSorted.valueAt(i).getPopularity();

            if(currentPop <= leastPop){
                leastPop = currentPop;
                leastPopularSite = i;
                System.out.println("lower: "+unknownSitesSorted.valueAt(i).getPosition());
            } else {
                System.out.println("over: "+unknownSitesSorted.valueAt(i).getPosition());
            }
        }*/

        //System.out.println(leastPopularSite);
        //recieveSite = unknownSitesSorted.valueAt(leastPopularSite);
        //unknownSitesSorted = unknownSitesSorted.clone();
        //unknownSitesSorted.remove(leastPopularSite);
        //System.out.println("removed");

        //System.out.println("recieve site: " + recieveSite);

        recieveSite = selectedUnknownSites.get(random);

        recieve_cid = recieveSite.getCid();

        recieveTitle.setText(recieveSite.getTitle());

        if(recieveSite.getFeature1().equals("0")){
            features1.setVisibility(View.GONE);
        }else{
            features1.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature2().equals("0")){
            features2.setVisibility(View.GONE);
        }else{
            features2.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature3().equals("0")){
            features3.setVisibility(View.GONE);
        }else{
            features3.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature4().equals("0")){
            features4.setVisibility(View.GONE);
        }else{
            features4.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature5().equals("0")){
            features5.setVisibility(View.GONE);
        }else{
            features5.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature6().equals("0")){
            features6.setVisibility(View.GONE);
        }else{
            features6.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature7().equals("0")){
            features7.setVisibility(View.GONE);
        }else{
            features7.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature8().equals("0")){
            features8.setVisibility(View.GONE);
        }else{
            features8.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature9().equals("0")){
            features9.setVisibility(View.GONE);
        }else{
            features9.setVisibility(View.VISIBLE);
        }
        if(recieveSite.getFeature10().equals("0")){
            features10.setVisibility(View.GONE);
        }else{
            features10.setVisibility(View.VISIBLE);
        }

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
            //on clicking register button move to Register Activity
            /*case R.id.btnCancel_Trade:

                Toast.makeText(this, "Trade Canceled!", Toast.LENGTH_LONG).show();
                //intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.btnRefresh_Trade:

                Random ran = new Random();
                if(nextSite == selectedUnknownSites.size()-1){
                    nextSite = 0;
                } else {
                    nextSite++;
                }
                //nextSite = getRandomWithExclusion(ran, 0, selectedUnknownSites.size()-1, nextSite);
                genUnknownSite(nextSite);
                break;

            case R.id.btnSend_Trade:
                //send trade
                new CreateTrade(this, send_cid, recieve_cid).execute();

                intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                intent.putExtra("trade", true);
                intent.putExtra("latitude", recieveSite.getPosition().latitude);
                intent.putExtra("longitude", recieveSite.getPosition().longitude);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_right:

                if(ownedSiteInit == ownedSitesSize-1){
                    ownedSiteInit = -1;
                }
                ownedSiteInit++;
                genOwnedSite(ownedSiteInit);

                break;

            case R.id.btn_left:

                if(ownedSiteInit == 0){
                    ownedSiteInit = ownedSitesSize;
                }
                ownedSiteInit--;
                genOwnedSite(ownedSiteInit);

                break;*/

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
                //nextSite = getRandomWithExclusion(ran, 0, selectedUnknownSites.size()-1, nextSite);
                genUnknownSite(nextSite);
                break;

            case R.id.yourSite:

                Intent i = new Intent(this, OwnedSiteActivity.class);
                i.putExtra("cid", send_cid);
                i.putExtra("prevState", 2);
                startActivity(i);
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
