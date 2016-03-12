package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


public class TradeView extends AppCompatActivity implements View.OnClickListener {

    ArrayList<LatLng> cluster = null;
    knownSite inst = new knownSite();
    int recieveSize;
    SparseArray<Site> selectedUnknownSites = new SparseArray<>();
    SparseArray<Site> knownMap;
    SparseArray<Site> unknownMap;
    Site recieveSite;
    Site sendSite;
    String send_cid;
    String recieve_cid;
    String unique_tid;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_viewer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            unique_tid = extras.getString("unique_tid");
            send_cid = extras.getString("send_cid");
            recieve_cid = extras.getString("recieve_cid");
        }

        Button cancel = (Button)findViewById(R.id.btnCancel_Trade);

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

        configSites();

        cancel.setOnClickListener(this);

    }

    public void configSites(){

        knownMap = inst.getKnownSitesMap();
        unknownMap = inst.getUnknownSitesMap();
        int sizeUnknown = inst.getUnknownSitesSize();
        int sizeKnown = inst.getSiteSize();

        for(int i=0; i<sizeUnknown; i++){
            if(unknownMap.get(i).getCid().equals(recieve_cid)){
                recieveSite = unknownMap.get(i);
            }
        }

        for(int j=0; j<sizeKnown; j++){
            if (knownMap.get(j).getCid().equals(send_cid)){
                sendSite = knownMap.get(j);
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

        sendRating.setRating((sendSite.getRating()).floatValue());

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel_Trade:

                //update trade record in db
                new CancelTrade(this, unique_tid).execute();

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
