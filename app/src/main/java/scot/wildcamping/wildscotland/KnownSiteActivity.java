package scot.wildcamping.wildscotland;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

/**
 * Created by Chris on 17-Mar-16.
 */
public class KnownSiteActivity extends AppCompatActivity implements View.OnClickListener {

    Double latitude;
    Double longitude;
    String cid;
    String titleBun;
    String descriptionBun;
    Double ratingBun;
    String feature1;
    String feature2;
    String feature3;
    String feature4;
    String feature5;
    String feature6;
    String feature7;
    String feature8;
    String feature9;
    String feature10;
    String imageStr;
    Bitmap imageBit;
    int prevState;
    int arrayPos;
    SparseArray<Site> known = new SparseArray<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_known_site);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Button contact = (Button)findViewById(R.id.contactSiteAdmin);
        TextView title = (TextView)findViewById(R.id.siteViewTitle);
        TextView description = (TextView)findViewById(R.id.siteViewDescription);
        RatingBar rating = (RatingBar)findViewById(R.id.siteViewRating);
        ImageView feature1Image = (ImageView)findViewById(R.id.preview_feature1);
        ImageView feature2Image = (ImageView)findViewById(R.id.preview_feature2);
        ImageView feature3Image = (ImageView)findViewById(R.id.preview_feature3);
        ImageView feature4Image = (ImageView)findViewById(R.id.preview_feature4);
        ImageView feature5Image = (ImageView)findViewById(R.id.preview_feature5);
        ImageView feature6Image = (ImageView)findViewById(R.id.preview_feature6);
        ImageView feature7Image = (ImageView)findViewById(R.id.preview_feature7);
        ImageView feature8Image = (ImageView)findViewById(R.id.preview_feature8);
        ImageView feature9Image = (ImageView)findViewById(R.id.preview_feature9);
        ImageView feature10Image = (ImageView)findViewById(R.id.preview_feature10);
        ImageView image = (ImageView)findViewById(R.id.image1);


        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            arrayPos = extras.getInt("arrayPosition");
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            cid = extras.getString("cid");
            titleBun = extras.getString("title");
            descriptionBun = extras.getString("description");
            ratingBun = extras.getDouble("rating");
            imageStr = extras.getString("image");
            prevState = extras.getInt("prevState");


            title.setText(titleBun);
            description.setText(descriptionBun);
            rating.setRating(ratingBun.floatValue());

            imageBit = StringToBitMap(imageStr);
            image.setImageBitmap(imageBit);
        }

        knownSite inst = new knownSite();
        known = inst.getKnownSitesMap();

        Site focused = known.get(arrayPos);

        feature1 = focused.getFeature1();
        feature2 = focused.getFeature2();
        feature3 = focused.getFeature3();
        feature4 = focused.getFeature4();
        feature5 = focused.getFeature5();
        feature6 = focused.getFeature6();
        feature7 = focused.getFeature7();
        feature8 = focused.getFeature8();
        feature9 = focused.getFeature9();
        feature10 = focused.getFeature10();

        if(feature1.equals("0")){
            feature1Image.setVisibility(View.GONE);
        }
        if(feature2.equals("0")){
            feature2Image.setVisibility(View.GONE);
        }
        if(feature3.equals("0")){
            feature3Image.setVisibility(View.GONE);
        }
        if(feature4.equals("0")){
            feature4Image.setVisibility(View.GONE);
        }
        if(feature5.equals("0")){
            feature5Image.setVisibility(View.GONE);
        }
        if(feature6.equals("0")){
            feature6Image.setVisibility(View.GONE);
        }
        if(feature7.equals("0")){
            feature7Image.setVisibility(View.GONE);
        }
        if(feature8.equals("0")){
            feature8Image.setVisibility(View.GONE);
        }
        if(feature9.equals("0")){
            feature9Image.setVisibility(View.GONE);
        }
        if(feature10.equals("0")){
            feature10Image.setVisibility(View.GONE);
        }

        contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.contactSiteAdmin:
                Intent i = new Intent(getApplicationContext(), ContactUser.class);
                startActivity(i);
                break;
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                if (prevState == 2){
                    intent.putExtra("fragment", 2);
                }
                startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
