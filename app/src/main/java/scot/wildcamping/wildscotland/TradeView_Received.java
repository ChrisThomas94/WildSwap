package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.AsyncTask.CreateNotification;
import scot.wildcamping.wildscotland.AsyncTask.FetchQuestions;
import scot.wildcamping.wildscotland.AsyncTask.FetchTradeRequests;
import scot.wildcamping.wildscotland.AsyncTask.UpdateTrade;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.knownSite;

/**
 * Created by Chris on 15-Mar-16.
 */
public class TradeView_Received extends AppCompatActivity implements View.OnClickListener {

    ArrayList<LatLng> cluster = null;
    knownSite inst = new knownSite();
    int recieveSize;
    SparseArray<Site> selectedUnknownSites = new SparseArray<>();
    SparseArray<Site> ownedMap;
    SparseArray<Site> knownMap;
    SparseArray<Site> unknownMap;
    Site recieveSite;
    Site sendSite;
    String send_cid;
    String recieve_cid;
    String recieve_token;
    String unique_tid;
    int PositiveTradeStatus = 2;
    int NegativeTradeStatus = 1;
    int status;

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
    ImageView sendPicture1;
    ImageView sendPicture2;
    ImageView sendPicture3;
    ImageView sendPicture4;

    String date;

    Boolean siteAlreadyOwned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_view_received);
        View parentLayout = findViewById(R.id.recieveTitle);

        siteAlreadyOwned = false;

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            unique_tid = extras.getString("unique_tid");
            send_cid = extras.getString("send_cid");
            recieve_cid = extras.getString("recieve_cid");
            date = extras.getString("date");
            status = extras.getInt("status");
        }

        //Button reject = (Button)findViewById(R.id.btnReject_Trade);
        //Button contactUser = (Button)findViewById(R.id.btnContact_User);
        //Button accept = (Button)findViewById(R.id.btnAccept_Trade);

        ImageView accept = (ImageView)findViewById(R.id.accept_trade);
        ImageView reject = (ImageView)findViewById(R.id.reject_trade);


        if(status != 0){
            reject.setVisibility(View.INVISIBLE);
            accept.setVisibility(View.INVISIBLE);
        }

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
        sendPicture1 = (ImageView)findViewById(R.id.sendPicture1);
        sendPicture2 = (ImageView)findViewById(R.id.sendPicture2);
        sendPicture3 = (ImageView)findViewById(R.id.sendPicture3);
        sendPicture4 = (ImageView)findViewById(R.id.sendPicture4);

        configSites();

        if(siteAlreadyOwned && status == 0){
            Snackbar.make(parentLayout, "Attention! You already own the site being offered!", Snackbar.LENGTH_LONG).show();
        }

        reject.setOnClickListener(this);
        //contactUser.setOnClickListener(this);
        accept.setOnClickListener(this);

    }

    public void configSites(){

        knownMap = inst.getKnownSitesMap();
        ownedMap = inst.getOwnedSitesMap();
        unknownMap = inst.getUnknownSitesMap();
        int sizeUnknown = inst.getUnknownSitesSize();
        System.out.println("size unknown: " + sizeUnknown);

        int sizeOwn = inst.getOwnedSiteSize();
        System.out.println("size own: " + sizeOwn);

        for(int i=0; i<sizeUnknown; i++){
            if(unknownMap.get(i).getCid().equals(send_cid)){
                recieveSite = unknownMap.get(i);
            } else {
                //error
            }
        }

        for(int j=0; j<sizeOwn; j++){
            if (ownedMap.get(j).getCid().equals(recieve_cid)){
                sendSite = ownedMap.get(j);
            }
        }

        System.out.print(recieveSite);
        if(recieveSite == null){
            System.out.println("recieveSite == null");
            for(int j=0; j<knownMap.size(); j++){
                if (knownMap.get(j).getCid().equals(send_cid)){
                    recieveSite = knownMap.get(j);
                    siteAlreadyOwned=true;
                }
            }
        }

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
            placeholderFeaturesYours.setVisibility(View.INVISIBLE);
        }

        sendRating.setRating((sendSite.getRating()).floatValue());

        recieveTitle.setText(recieveSite.getTitle());

        recieve_token = recieveSite.getToken();

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
            placeholderFeatures.setVisibility(View.INVISIBLE);
        }

        recieveRating.setRating((recieveSite.getRating()).floatValue());

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.accept_trade:

                //update trade record in db positively
                new UpdateTrade(this, unique_tid, PositiveTradeStatus).execute();
                new CreateNotification(this, recieve_token).execute();

                //create new entry in user_has_trades with relat 45

                intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                startActivity(intent);
                finish();
                break;


            case R.id.reject_trade:

                //update trade record in db
                new UpdateTrade(this, unique_tid, NegativeTradeStatus).execute();
                new CreateNotification(this, recieve_token).execute();

                if(isNetworkAvailable()) {
                    try {
                        String trades_result = new FetchTradeRequests(this).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent i = new Intent(getApplicationContext(), TradesActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;

            /*case R.id.btnReject_Trade:

                //update trade record in db
                new UpdateTrade(this, unique_tid, NegativeTradeStatus).execute();
                if(isNetworkAvailable()) {
                    try {
                        String trades_result = new FetchTradeRequests(this).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                Intent i = new Intent(getApplicationContext(), TradesActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.btnContact_User:
                //intent = new Intent(getApplicationContext(), ContactUser.class);
                //intent.putExtra("contact", recieveSite.getSiteAdmin());
                //intent.putExtra("date", date);

                //open email dialog
                intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recieveSite.getSiteAdmin()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Wild Scotland - Trade");
                intent.putExtra(Intent.EXTRA_TEXT, "Hello fellow wild camper, I am contacting you because...");
                startActivity(intent);
                break;

            case R.id.btnAccept_Trade:

                //update trade record in db positively
                new UpdateTrade(this, unique_tid, PositiveTradeStatus).execute();

                //create new entry in user_has_trades with relat 45

                intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                startActivity(intent);
                finish();
                break;*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_trade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

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
                if(isNetworkAvailable()) {
                    try {
                        String questions = new FetchQuestions(this, recieveSite.getSiteAdmin()).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }
                //open that user's profile
                Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
                in.putExtra("user", recieveSite.getSiteAdmin());
                in.putExtra("this_user", false);
                //intent.putExtra("date", date); instance of date
                startActivity(in);
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
