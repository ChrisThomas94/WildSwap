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

    RelativeLayout nearbyTerrainLayout;
    RelativeLayout immediateTerrainLayout;

    ImageView feature1circle;
    ImageView feature2circle;
    ImageView feature3circle;
    ImageView feature4circle;
    ImageView feature5circle;
    ImageView feature6circle;
    ImageView feature7circle;
    ImageView feature8circle;
    ImageView feature9circle;

    ImageView feature1Thumbnail;
    ImageView feature2Thumbnail;
    ImageView feature3Thumbnail;
    ImageView feature4Thumbnail;
    ImageView feature5Thumbnail;
    ImageView feature6Thumbnail;
    ImageView feature7Thumbnail;
    ImageView feature8Thumbnail;
    ImageView feature9Thumbnail;

    TextView distant;
    TextView nearby;
    TextView immediate;
    TextView instructions;

    Boolean selectDistantTerrain = true;
    Boolean selectNearbyTerrain = false ;
    Boolean selectImmediateTerrain = false;

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

        nearbyTerrainLayout = (RelativeLayout)findViewById(R.id.nearbyTerrainFeaturesLayout);
        immediateTerrainLayout = (RelativeLayout)findViewById(R.id.immediateTerrainFeaturesLayout);

        feature1circle = (ImageView)findViewById(R.id.feature1circle);
        feature2circle = (ImageView)findViewById(R.id.feature2circle);
        feature3circle = (ImageView)findViewById(R.id.feature3circle);
        feature4circle = (ImageView)findViewById(R.id.feature4circle);
        feature5circle = (ImageView)findViewById(R.id.feature5circle);

        feature1Thumbnail = (ImageView)findViewById(R.id.feature1thumbnail);
        feature2Thumbnail = (ImageView)findViewById(R.id.feature2thumbnail);
        feature3Thumbnail = (ImageView)findViewById(R.id.feature3thumbnail);
        feature4Thumbnail = (ImageView)findViewById(R.id.feature4thumbnail);
        feature5Thumbnail = (ImageView)findViewById(R.id.feature5thumbnail);
        feature6Thumbnail = (ImageView)findViewById(R.id.feature6thumbnail);
        feature7Thumbnail = (ImageView)findViewById(R.id.feature7thumbnail);
        feature8Thumbnail = (ImageView)findViewById(R.id.feature8thumbnail);
        feature9Thumbnail = (ImageView)findViewById(R.id.feature9thumbnail);

        distant = (TextView)findViewById(R.id.distantPlaceholderText);
        nearby = (TextView)findViewById(R.id.nearbyPlaceholderText);
        immediate = (TextView)findViewById(R.id.immediatePlaceholderText);
        instructions = (TextView)findViewById(R.id.instructions);

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

        confirmFeatures = (Button)findViewById(R.id.confirmFeatures);
        cancelFeatures = (Button)findViewById(R.id.cancelFeatures);

        feature1box.setOnClickListener(this);
        feature2box.setOnClickListener(this);
        feature3box.setOnClickListener(this);
        feature4box.setOnClickListener(this);
        feature5box.setOnClickListener(this);
        feature6box.setOnClickListener(this);
        feature7box.setOnClickListener(this);
        feature8box.setOnClickListener(this);
        feature9box.setOnClickListener(this);

        confirmFeatures.setOnClickListener(this);
        cancelFeatures.setOnClickListener(this);

        feature1Thumbnail.setImageResource(R.drawable.hill_thumbnail);
        feature2Thumbnail.setImageResource(R.drawable.forestthumbnail);
        feature3Thumbnail.setImageResource(R.drawable.plainsthumbnail);
        feature4Thumbnail.setImageResource(R.drawable.mountains_thumbnail);
        feature5Thumbnail.setImageResource(R.drawable.oceanthumbnail);
        feature6Thumbnail.setImageResource(0);
        feature7Thumbnail.setImageResource(0);
        feature8Thumbnail.setImageResource(0);
        feature9Thumbnail.setImageResource(0);
    }

    public void resetProgressCircle(ImageView circle1, ImageView circle2, ImageView circle3, ImageView circle4, ImageView circle5){

        //reset all progress circles
        circle1.setImageResource(0);
        circle2.setImageResource(0);
        circle3.setImageResource(0);
        circle4.setImageResource(0);
        circle5.setImageResource(0);

    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            //hills
            //hills
            //grass
            case R.id.feature1box:

                if(selectDistantTerrain || selectNearbyTerrain) {

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;

                    if (feature1count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.hill1);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.hill1nearby);
                        }

                        feature1circle.setImageResource(R.drawable.quarter);
                        feature1count++;

                    } else if (feature1count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setImageResource(R.drawable.hill2);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.hill2nearby);
                        }

                        feature1circle.setImageResource(R.drawable.half);
                        feature1count++;

                    } else if (feature1count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.hill3);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.hill3nearby);
                        }
                        feature1circle.setImageResource(R.drawable.threequartercurve);
                        feature1count++;

                    } else if (feature1count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.hill4);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.hill4nearby);
                        }
                        feature1circle.setImageResource(R.drawable.full);
                        feature1count++;

                    } else {
                        feature1count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.hill1);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.hill1nearby);
                        }
                        feature1circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                    immediateTerrain.setImageResource(R.drawable.grass);
                }

                break;

            //forest
            //forest
            //pebbles
            case R.id.feature2box:
                if(selectDistantTerrain || selectNearbyTerrain) {

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);
                    feature1count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;

                    if (feature2count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest1);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest1nearby);
                        }

                        feature2circle.setImageResource(R.drawable.quarter);
                        feature2count++;

                    } else if (feature2count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setImageResource(R.drawable.forest2);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest2nearby);
                        }

                        feature2circle.setImageResource(R.drawable.half);
                        feature2count++;

                    } else if (feature2count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest3);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest3nearby);
                        }
                        feature2circle.setImageResource(R.drawable.threequartercurve);
                        feature2count++;

                    } else if (feature2count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest4);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest4nearby);
                        }
                        feature2circle.setImageResource(R.drawable.full);
                        feature2count++;

                    } else {
                        feature2count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest1);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest1nearby);
                        }
                        feature2circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                    immediateTerrain.setImageResource(R.drawable.pebbles);
                }
                break;

            //plains
            //plains
            //sand
            case R.id.feature3box:
                if(selectDistantTerrain || selectNearbyTerrain) {

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature4count = 0;
                    feature5count = 0;

                    if (feature3count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains1nearby);
                        }

                        feature3circle.setImageResource(R.drawable.quarter);
                        feature3count++;

                    } else if (feature3count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setImageResource(R.drawable.plains2);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains2nearby);
                        }

                        feature3circle.setImageResource(R.drawable.half);
                        feature3count++;

                    } else if (feature3count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains3);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains3nearby);
                        }
                        feature3circle.setImageResource(R.drawable.threequartercurve);
                        feature3count++;

                    } else if (feature3count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains4);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains4nearby);
                        }
                        feature3circle.setImageResource(R.drawable.full);
                        feature3count++;

                    } else {
                        feature3count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains);
                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains1nearby);
                        }
                        feature3circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                    immediateTerrain.setImageResource(R.drawable.pebbles);
                }
                break;

            case R.id.feature4box:
                if(selectDistantTerrain) {distant.setVisibility(GONE);

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature5count = 0;

                    if (feature4count == 0) {
                        distantTerrain.setImageResource(R.drawable.mountains);
                        feature4circle.setImageResource(R.drawable.quarter);
                        feature4count++;

                    } else if (feature4count == 1) {
                        distantTerrain.setImageResource(R.drawable.mountains2);
                        feature4circle.setImageResource(R.drawable.half);
                        feature4count++;

                    } else if (feature4count == 2) {
                        distantTerrain.setImageResource(R.drawable.mountains3);
                        feature4circle.setImageResource(R.drawable.threequartercurve);
                        feature4count++;

                    } else if (feature4count == 3) {
                        distantTerrain.setImageResource(R.drawable.mountains4);
                        feature4circle.setImageResource(R.drawable.full);
                        feature4count++;

                    } else {
                        feature4count = 1;
                        distantTerrain.setImageResource(R.drawable.mountains);
                        feature4circle.setImageResource(R.drawable.quarter);
                    }

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                }
                break;

            case R.id.feature5box:
                if(selectDistantTerrain) {
                    distant.setVisibility(GONE);
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;

                    distantTerrain.setImageResource(R.drawable.ocean);
                    feature5circle.setImageResource(R.drawable.full);
                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                }
                break;

            case R.id.feature6box:
                if(selectDistantTerrain) {

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                }
                break;

            case R.id.feature7box:
                if(selectDistantTerrain) {

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                }
                break;

            case R.id.feature8box:
                if(selectDistantTerrain) {

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                }
                break;

            case R.id.feature9box:
                if(selectDistantTerrain) {

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);

                }
                break;

            case R.id.cancelFeatures:
                Intent intent = new Intent(getApplicationContext(), AddSite.class);

                startActivity(intent);
                finish();
                break;

            case R.id.confirmFeatures:

                if(selectDistantTerrain){
                    selectDistantTerrain = false;
                    selectNearbyTerrain = true;
                    instructions.setText(R.string.nearbyTerrain);
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);
                    //change thumbnail images?
                    feature1Thumbnail.setImageResource(R.drawable.hill_thumbnail);
                    feature2Thumbnail.setImageResource(R.drawable.forestthumbnail);
                    feature3Thumbnail.setImageResource(R.drawable.plainsthumbnail);
                    feature4Thumbnail.setImageResource(0);
                    feature5Thumbnail.setImageResource(0);
                    feature6Thumbnail.setImageResource(0);
                    feature7box.setVisibility(GONE);
                    feature8box.setVisibility(GONE);
                    feature9box.setVisibility(GONE);

                    nearbyTerrainLayout.setVisibility(View.VISIBLE);

                } else if (selectNearbyTerrain){
                    selectNearbyTerrain = false;
                    selectImmediateTerrain = true;
                    instructions.setText(R.string.immediateTerrain);
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle);

                    feature1Thumbnail.setImageResource(R.drawable.grassthumbnail);
                    feature2box.setVisibility(GONE);
                    feature3box.setVisibility(GONE);
                    feature4box.setVisibility(GONE);
                    feature5box.setVisibility(GONE);
                    feature6box.setVisibility(GONE);
                    feature7box.setVisibility(GONE);
                    feature8box.setVisibility(GONE);
                    feature9box.setVisibility(GONE);

                    immediateTerrainLayout.setVisibility(View.VISIBLE);
                }

                /*if(end)
                Intent i = new Intent(getApplicationContext(), AddSite.class);

                bundle the various selections.
                i.e. distant terrain
                nearby terrain etc...
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
                finish();*/
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
