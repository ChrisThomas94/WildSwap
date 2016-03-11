package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chris on 11-Mar-16.
 */
public class TradeActivitySimple extends Activity implements View.OnClickListener {

    public final MediaType JSON
            = MediaType.parse("application/json;  charset=utf-8"); // charset=utf-8

    OkHttpClient client = new OkHttpClient();

    ArrayList<LatLng> cluster = null;
    knownSite inst = new knownSite();
    int recieveSize;
    SparseArray<Site> selectedUnknownSites = new SparseArray<>();
    SparseArray<Site> knownMap;
    SparseArray<Site> unknownMap;
    Site recieveSite;
    Site sendSite;

    TextView recieveTitle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_simple);

        knownMap = inst.getKnownSitesMap();
        unknownMap = inst.getUnknownSitesMap();
        int size = inst.getUnknownSitesSize();

        Button cancel = (Button)findViewById(R.id.btnCancel_Trade);
        Button refresh = (Button)findViewById(R.id.btnRefresh_Trade);
        Button send = (Button)findViewById(R.id.btnSend_Trade);

        recieveTitle = (TextView)findViewById(R.id.recieveTitle);
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


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cluster = extras.getParcelableArrayList("cluster");
            recieveSize = cluster.size();
            System.out.println("Trade" + cluster + recieveSize);
        }

        //where lat lng of cluster = lat lng of map
        //add to new map

        for(int i=0; i<recieveSize; i++){

            for(int j=0; j<size; j++){
                Site currentSite = unknownMap.get(j);

                if (cluster.get(i).equals(currentSite.getPosition())) {
                    System.out.println(currentSite.getPosition());
                    selectedUnknownSites.put(i, currentSite);
                }
            }
        }

        //initialise unknown site
        genUnknownSite(0);

        //initialise known site
        genKnownSite(0);

        cancel.setOnClickListener(this);
        refresh.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    public void genKnownSite(int random){
        sendSite = knownMap.get(random);

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

        sendRating.setRating((sendSite.getRating()).floatValue());
    }

    public void genUnknownSite(int random){
        recieveSite = selectedUnknownSites.get(random);

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

        recieveRating.setRating((recieveSite.getRating()).floatValue());
    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            //on clicking register button move to Register Activity
            case R.id.btnCancel_Trade:
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.btnRefresh_Trade:
                Random ran = new Random();
                int nextSite = ran.nextInt(recieveSize);
                genUnknownSite(nextSite);
                break;

            case R.id.btnSend_Trade:
                //send trade
                new SendTrade(this, sendSite, recieveSite).execute();
                break;
        }
    }

}
