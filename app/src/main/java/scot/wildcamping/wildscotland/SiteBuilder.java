package scot.wildcamping.wildscotland;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.GONE;

/**
 * Created by Chris on 03-Mar-16.
 */
public class SiteBuilder extends AppCompatActivity implements View.OnClickListener {

    ImageView distantTerrain;
    ImageView nearbyTerrain;
    ImageView immediateTerrain;

    ImageView feature1circle;
    ImageView feature2circle;
    ImageView feature3circle;
    ImageView feature4circle;
    ImageView feature5circle;
    ImageView feature6circle;
    ImageView feature7circle;
    ImageView feature8circle;
    ImageView feature9circle;


    TextView distant;
    TextView nearby;
    TextView immediate;

    int feature1count = 0;
    int feature2count = 0;
    int feature3count = 0;
    int feature4count = 0;
    int feature5count = 0;
    int feature6count = 0;
    int feature7count = 0;
    int feature8count = 0;
    int feature9count = 0;


    RelativeLayout feature1box;
    RelativeLayout feature2box;
    RelativeLayout feature3box;
    RelativeLayout feature4box;
    RelativeLayout feature5box;
    RelativeLayout feature6box;
    RelativeLayout feature7box;
    RelativeLayout feature8box;
    RelativeLayout feature9box;
    RelativeLayout feature10box;
    Boolean feature1 = false;
    Boolean feature2 = false;
    Boolean feature3 = false;
    Boolean feature4 = false;
    Boolean feature5 = false;
    Boolean feature6 = false;
    Boolean feature7 = false;
    Boolean feature8 = false;
    Boolean feature9 = false;
    Boolean feature10 = false;

    Boolean newFeature1 = false;
    Boolean newFeature2 = false;
    Boolean newFeature3 = false;
    Boolean newFeature4 = false;
    Boolean newFeature5 = false;
    Boolean newFeature6 = false;
    Boolean newFeature7 = false;
    Boolean newFeature8 = false;
    Boolean newFeature9 = false;
    Boolean newFeature10 = false;

    Boolean image;
    int arrayPos;
    Boolean update;

    Button confirmFeatures;
    Button cancelFeatures;

    double latitude;
    double longitude;
    String title;
    String description;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_builder);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        final int green = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        final int gray = ContextCompat.getColor(getApplicationContext(), R.color.counter_text_color);

        distantTerrain = (ImageView)findViewById(R.id.distantTerrainFeatures);
        nearbyTerrain = (ImageView)findViewById(R.id.nearbyTerrainFeatures);
        immediateTerrain = (ImageView)findViewById(R.id.immediateTerrainFeatures);

        feature1circle = (ImageView)findViewById(R.id.feature1circle);
        feature2circle = (ImageView)findViewById(R.id.feature2circle);

        distant = (TextView)findViewById(R.id.distantPlaceholderText);

        feature1box = (RelativeLayout)findViewById(R.id.feature1box);
        feature2box = (RelativeLayout)findViewById(R.id.feature2box);
        feature3box = (RelativeLayout)findViewById(R.id.feature3box);
        feature4box = (RelativeLayout)findViewById(R.id.feature4box);
        feature5box = (RelativeLayout)findViewById(R.id.feature5box);
        feature6box = (RelativeLayout)findViewById(R.id.feature6box);
        feature7box = (RelativeLayout)findViewById(R.id.feature7box);
        feature8box = (RelativeLayout)findViewById(R.id.feature8box);
        feature9box = (RelativeLayout)findViewById(R.id.feature9box);
        feature10box = (RelativeLayout)findViewById(R.id.feature10box);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            //bundled from update
            update =  extras.getBoolean("update");
            if(update) {
                arrayPos = extras.getInt("arrayPosition");

            } else {
                //bundled from add
                image = extras.getBoolean("image");
                latitude = extras.getDouble("latitude");
                longitude = extras.getDouble("longitude");
                title = extras.getString("title");
                description = extras.getString("description");
                rating = extras.getFloat("rating");
            }

            feature1 = extras.getBoolean("feature1");
            feature2 = extras.getBoolean("feature2");
            feature3 = extras.getBoolean("feature3");
            feature4 = extras.getBoolean("feature4");
            feature5 = extras.getBoolean("feature5");
            feature6 = extras.getBoolean("feature6");
            feature7 = extras.getBoolean("feature7");
            feature8 = extras.getBoolean("feature8");
            feature9 = extras.getBoolean("feature9");
            feature10 = extras.getBoolean("feature10");
            newFeature1 = extras.getBoolean("feature1");
            newFeature2 = extras.getBoolean("feature2");
            newFeature3 = extras.getBoolean("feature3");
            newFeature4 = extras.getBoolean("feature4");
            newFeature5 = extras.getBoolean("feature5");
            newFeature6 = extras.getBoolean("feature6");
            newFeature7 = extras.getBoolean("feature7");
            newFeature8 = extras.getBoolean("feature8");
            newFeature9 = extras.getBoolean("feature9");
            newFeature10 = extras.getBoolean("feature10");
        }

        if(feature1){
            feature1box.setBackgroundColor(green);
        }
        if(feature2){
            feature2box.setBackgroundColor(green);
        }
        if(feature3){
            feature3box.setBackgroundColor(green);
        }
        if(feature4){
            feature4box.setBackgroundColor(green);
        }
        if(feature5){
            feature5box.setBackgroundColor(green);
        }
        if(feature6){
            feature6box.setBackgroundColor(green);
        }
        if(feature7){
            feature7box.setBackgroundColor(green);
        }
        if(feature8){
            feature8box.setBackgroundColor(green);
        }
        if(feature9){
            feature9box.setBackgroundColor(green);
        }
        if(feature10){
            feature10box.setBackgroundColor(green);
        }

        confirmFeatures = (Button)findViewById(R.id.confirmFeatures);
        cancelFeatures = (Button)findViewById(R.id.cancelFeatures);

        confirmFeatures.setOnClickListener(this);
        cancelFeatures.setOnClickListener(this);

        feature1box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distant.setVisibility(GONE);
                resetProgressCircle(feature1circle, feature2circle);
                feature2count = 0;
                if(feature1count == 0){
                    distantTerrain.setImageResource(R.drawable.hill1);
                    feature1circle.setImageResource(R.drawable.quarter);
                    feature1count++;

                } else if(feature1count == 1) {
                    distantTerrain.setImageResource(R.drawable.hill2);
                    feature1circle.setImageResource(R.drawable.half);
                    feature1count++;

                } else if(feature1count == 2) {
                    distantTerrain.setImageResource(R.drawable.hill3);
                    feature1circle.setImageResource(R.drawable.threequarter);
                    feature1count++;

                } else if(feature1count == 3) {
                    distantTerrain.setImageResource(R.drawable.hill4);
                    feature1circle.setImageResource(R.drawable.full);
                    feature1count++;

                } else {
                    feature1count = 1;
                    distantTerrain.setImageResource(R.drawable.hill1);
                    feature1circle.setImageResource(R.drawable.quarter);
                }
            }
        });

        feature2box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distant.setVisibility(GONE);
                resetProgressCircle(feature1circle, feature2circle);
                feature1count = 0;
                if(feature2count == 0){
                    distantTerrain.setImageResource(R.drawable.mountains);
                    feature2circle.setImageResource(R.drawable.quarter);
                    feature2count++;

                } else if(feature2count == 1) {
                    distantTerrain.setImageResource(R.drawable.mountains2);
                    feature2circle.setImageResource(R.drawable.half);
                    feature2count++;

                } else if(feature2count == 2) {
                    distantTerrain.setImageResource(R.drawable.mountains3);
                    feature2circle.setImageResource(R.drawable.threequarter);
                    feature2count++;

                } else if(feature2count == 3) {
                    distantTerrain.setImageResource(R.drawable.mountains4);
                    feature2circle.setImageResource(R.drawable.full);
                    feature2count++;

                } else {
                    feature2count = 1;
                    distantTerrain.setImageResource(R.drawable.mountains);
                    feature2circle.setImageResource(R.drawable.quarter);
                }
            }
        });

        feature3box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature3){
                    //turn colour grey
                    //feature3box.setBackgroundColor(gray);
                    newFeature3 = false;
                } else {
                    //turn colour green
                    //feature3box.setBackgroundColor(green);
                    distantTerrain.setImageResource(R.drawable.plains);
                    newFeature3 = true;
                }
            }
        });

        feature4box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature4){
                    //turn colour grey
                    //feature4box.setBackgroundColor(gray);
                    newFeature4 = false;
                } else {
                    //turn colour green
                    //feature4box.setBackgroundColor(green);
                    distantTerrain.setImageResource(R.drawable.forest1);
                    newFeature4 = true;
                }
            }
        });

        feature5box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature5){
                    //turn colour grey
                    //feature5box.setBackgroundColor(gray);
                    newFeature5 = false;
                } else {
                    //turn colour green
                    //feature5box.setBackgroundColor(green);
                    distantTerrain.setImageResource(R.drawable.ocean);
                    newFeature5 = true;
                }
            }
        });

        feature6box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature6){
                    //turn colour grey
                    //feature6box.setBackgroundColor(gray);
                    newFeature6 = false;
                } else {
                    //turn colour green
                    //feature6box.setBackgroundColor(green);
                    newFeature6 = true;
                }
            }
        });

        feature7box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature7){
                    //turn colour grey
                    //feature7box.setBackgroundColor(gray);
                    newFeature7 = false;
                } else {
                    //turn colour green
                    //feature7box.setBackgroundColor(green);
                    newFeature7 = true;
                }
            }
        });

        feature8box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature8){
                    //turn colour grey
                    //feature8box.setBackgroundColor(gray);
                    newFeature8 = false;
                } else {
                    //turn colour green
                    //feature8box.setBackgroundColor(green);
                    newFeature8 = true;
                    //display snackbar
                    //Snackbar.make(v, "Warning, users may be able to figure out the location of your site based on this feature!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        feature9box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newFeature9){
                    //turn colour grey
                    //feature9box.setBackgroundColor(gray);
                    newFeature9 = false;
                } else {
                    //turn colour green
                    //feature9box.setBackgroundColor(green);
                    newFeature9 = true;
                    //Snackbar.make(v, "Warning, users may be able to figure out the location of your site based on this feature!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    public void resetProgressCircle(ImageView circle1, ImageView circle2){

        //reset all progress circles
        circle1.setImageResource(0);
        circle2.setImageResource(0);

    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            case R.id.cancelFeatures:
                Intent intent;
                if(update){
                    intent = new Intent(getApplicationContext(), UpdateSiteActivity.class);

                    //bundle for update
                    intent.putExtra("arrayPosition", arrayPos);
                    intent.putExtra("feature1", feature1);
                    intent.putExtra("feature2", feature2);
                    intent.putExtra("feature3", feature3);
                    intent.putExtra("feature4", feature4);
                    intent.putExtra("feature5", feature5);
                    intent.putExtra("feature6", feature6);
                    intent.putExtra("feature7", feature7);
                    intent.putExtra("feature8", feature8);
                    intent.putExtra("feature9", feature9);
                    intent.putExtra("feature10", feature10);
                } else {
                    intent = new Intent(getApplicationContext(), AddSite.class);

                    //bundle for add
                    intent.putExtra("image", image);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    intent.putExtra("rating", rating);
                }

                startActivity(intent);
                finish();
                break;

            case R.id.confirmFeatures:

                Intent i;
                if(update){
                    i = new Intent(getApplicationContext(), UpdateSiteActivity.class);

                    //bundle for update
                    i.putExtra("arrayPosition", arrayPos);
                } else {
                    i = new Intent(getApplicationContext(), AddSite.class);

                    //bundle for add
                    i.putExtra("image", image);
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("title", title);
                    i.putExtra("description", description);
                    i.putExtra("rating", rating);
                }

                i.putExtra("feature1", newFeature1);
                i.putExtra("feature2", newFeature2);
                i.putExtra("feature3", newFeature3);
                i.putExtra("feature4", newFeature4);
                i.putExtra("feature5", newFeature5);
                i.putExtra("feature6", newFeature6);
                i.putExtra("feature7", newFeature7);
                i.putExtra("feature8", newFeature8);
                i.putExtra("feature9", newFeature9);
                i.putExtra("feature10", newFeature10);

                startActivity(i);
                finish();
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                Intent intent;
                if(update){
                    intent = new Intent(getApplicationContext(), UpdateSiteActivity.class);

                    //bundle for update
                    intent.putExtra("arrayPosition", arrayPos);
                    intent.putExtra("feature1", feature1);
                    intent.putExtra("feature2", feature2);
                    intent.putExtra("feature3", feature3);
                    intent.putExtra("feature4", feature4);
                    intent.putExtra("feature5", feature5);
                    intent.putExtra("feature6", feature6);
                    intent.putExtra("feature7", feature7);
                    intent.putExtra("feature8", feature8);
                    intent.putExtra("feature9", feature9);
                    intent.putExtra("feature10", feature10);
                } else {
                    intent = new Intent(getApplicationContext(), AddSite.class);

                    //bundle for add
                    intent.putExtra("image", image);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    intent.putExtra("rating", rating);
                }

                startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
