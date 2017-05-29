package scot.wildcamping.wildswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.GONE;

/**
 * Created by Chris on 03-Mar-16.
 *
 */
public class SiteBuilderActivity extends AppCompatActivity implements View.OnClickListener {

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

    TextView feature1Text;
    TextView feature2Text;
    TextView feature3Text;
    TextView feature4Text;
    TextView feature5Text;
    TextView feature6Text;
    TextView feature7Text;
    TextView feature8Text;
    TextView feature9Text;

    FrameLayout feature4dimmer;
    FrameLayout feature5dimmer;
    FrameLayout feature6dimmer;


    TextView distant;
    TextView nearby;
    TextView immediate;
    TextView instructions;

    int distantSelected;
    int nearbySelected;
    int immediateSelected;

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

    int arrayPos;
    Boolean update = false;

    Button confirmSelection;

    Boolean image;
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

        }

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
        feature6circle = (ImageView)findViewById(R.id.feature6circle);
        feature7circle = (ImageView)findViewById(R.id.feature7circle);
        feature8circle = (ImageView)findViewById(R.id.feature8circle);
        feature9circle = (ImageView)findViewById(R.id.feature9circle);

        feature1Thumbnail = (ImageView)findViewById(R.id.feature1thumbnail);
        feature2Thumbnail = (ImageView)findViewById(R.id.feature2thumbnail);
        feature3Thumbnail = (ImageView)findViewById(R.id.feature3thumbnail);
        feature4Thumbnail = (ImageView)findViewById(R.id.feature4thumbnail);
        feature5Thumbnail = (ImageView)findViewById(R.id.feature5thumbnail);
        feature6Thumbnail = (ImageView)findViewById(R.id.feature6thumbnail);
        feature7Thumbnail = (ImageView)findViewById(R.id.feature7thumbnail);
        feature8Thumbnail = (ImageView)findViewById(R.id.feature8thumbnail);
        feature9Thumbnail = (ImageView)findViewById(R.id.feature9thumbnail);

        feature4dimmer = (FrameLayout) findViewById(R.id.feature4dimmer);
        feature5dimmer = (FrameLayout) findViewById(R.id.feature5dimmer);
        feature6dimmer = (FrameLayout) findViewById(R.id.feature6dimmer);

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

        feature1Text = (TextView)findViewById(R.id.feature1Text);
        feature2Text = (TextView)findViewById(R.id.feature2Text);
        feature3Text = (TextView)findViewById(R.id.feature3Text);
        feature4Text = (TextView)findViewById(R.id.feature4Text);
        feature5Text = (TextView)findViewById(R.id.feature5Text);
        feature6Text = (TextView)findViewById(R.id.feature6Text);
        feature7Text = (TextView)findViewById(R.id.feature7Text);
        feature8Text = (TextView)findViewById(R.id.feature8Text);
        feature9Text = (TextView)findViewById(R.id.feature9Text);

        confirmSelection = (Button)findViewById(R.id.confirmSelection);

        feature1box.setOnClickListener(this);
        feature2box.setOnClickListener(this);
        feature3box.setOnClickListener(this);
        feature4box.setOnClickListener(this);
        feature5box.setOnClickListener(this);
        feature6box.setOnClickListener(this);
        feature7box.setOnClickListener(this);
        feature8box.setOnClickListener(this);
        feature9box.setOnClickListener(this);

        confirmSelection.setOnClickListener(this);
        confirmSelection.setVisibility(GONE);

        feature1Thumbnail.setImageResource(R.drawable.hillthumbnail);
        feature1Text.setText(R.string.distantFeature1);

        feature2Thumbnail.setImageResource(R.drawable.forestthumbnail);
        feature2Text.setText(R.string.distantFeature2);

        feature3Thumbnail.setImageResource(R.drawable.plainsthumbnail);
        feature3Text.setText(R.string.distantFeature3);

        feature4Thumbnail.setImageResource(R.drawable.lochthumbnail);
        feature4Text.setText(R.string.distantFeature4);

        feature5Thumbnail.setImageResource(R.drawable.mountains_thumbnail);
        feature5Text.setText(R.string.distantFeature5);

        feature6Thumbnail.setImageResource(R.drawable.valleythumbnail);
        feature6Text.setText(R.string.distantFeature6);

        feature7Thumbnail.setImageResource(R.drawable.oceanthumbnail);
        feature7Text.setText(R.string.distantFeature7);

        feature8Thumbnail.setImageResource(0);
        feature8box.setVisibility(GONE);

        feature9Thumbnail.setImageResource(0);
        feature9box.setVisibility(GONE);

        feature4dimmer.getForeground().setAlpha(0);
        feature5dimmer.getForeground().setAlpha(0);
        feature6dimmer.getForeground().setAlpha(0);

    }

    public void resetProgressCircle(ImageView circle1, ImageView circle2, ImageView circle3, ImageView circle4, ImageView circle5, ImageView circle6, ImageView circle7){

        //reset all progress circles
        circle1.setImageResource(0);
        circle2.setImageResource(0);
        circle3.setImageResource(0);
        circle4.setImageResource(0);
        circle5.setImageResource(0);
        circle6.setImageResource(0);
        circle7.setImageResource(0);

    }

    @Override
    public void onClick(View v){

        switch (v.getId())
        {
            //hills
            //hills
            //grass
            case R.id.feature1box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain || selectNearbyTerrain) {

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                        nearbyTerrainLayout.setVisibility(View.VISIBLE);

                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;
                    feature6count = 0;
                    feature7count = 0;


                    if (feature1count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setTag("hill1");
                            distantTerrain.setImageResource(R.drawable.hill1);

                        } else {
                            nearbyTerrain.setTag("hill1nearby");
                            nearbyTerrain.setImageResource(R.drawable.hill1nearby);
                        }

                        feature1circle.setImageResource(R.drawable.quarter);
                        feature1count++;

                    } else if (feature1count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setTag("hill2");
                            distantTerrain.setImageResource(R.drawable.hill2);

                        } else {
                            nearbyTerrain.setTag("hill2nearby");
                            nearbyTerrain.setImageResource(R.drawable.hill2nearby);

                        }

                        feature1circle.setImageResource(R.drawable.half);
                        feature1count++;

                    } else if (feature1count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setTag("hill3");
                            distantTerrain.setImageResource(R.drawable.hill3);

                        } else {
                            nearbyTerrain.setTag("hill3nearby");
                            nearbyTerrain.setImageResource(R.drawable.hill3nearby);

                        }
                        feature1circle.setImageResource(R.drawable.threequartercurve);
                        feature1count++;

                    } else if (feature1count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setTag("hill4");
                            distantTerrain.setImageResource(R.drawable.hill4);

                        } else {
                            nearbyTerrain.setTag("hill4nearby");
                            nearbyTerrain.setImageResource(R.drawable.hill4nearby);

                        }
                        feature1circle.setImageResource(R.drawable.full);
                        feature1count++;

                    } else {
                        feature1count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setTag("hill1");
                            distantTerrain.setImageResource(R.drawable.hill1);

                        } else {
                            nearbyTerrain.setTag("hill1nearby");
                            nearbyTerrain.setImageResource(R.drawable.hill1nearby);

                        }
                        feature1circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain){
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature2count = 0;
                    feature3count = 0;

                    //immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                    immediateTerrain.setImageResource(R.drawable.grass);
                    immediateTerrain.setTag("grass");
                    feature1circle.setImageResource(R.drawable.full);
                }

                break;

            //forest
            //forest
            //pebbles
            case R.id.feature2box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain || selectNearbyTerrain) {

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                        nearbyTerrainLayout.setVisibility(View.VISIBLE);
                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;
                    feature6count = 0;
                    feature7count = 0;

                    if (feature2count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest1);
                            distantTerrain.setTag("forest1");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest1nearby);
                            nearbyTerrain.setTag("forest1nearby");

                        }

                        feature2circle.setImageResource(R.drawable.quarter);
                        feature2count++;

                    } else if (feature2count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setImageResource(R.drawable.forest2);
                            distantTerrain.setTag("forest2");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest2nearby);
                            nearbyTerrain.setTag("forest2nearby");

                        }

                        feature2circle.setImageResource(R.drawable.half);
                        feature2count++;

                    } else if (feature2count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest3);
                            distantTerrain.setTag("forest3");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest3nearby);
                            nearbyTerrain.setTag("forest3nearby");

                        }
                        feature2circle.setImageResource(R.drawable.threequartercurve);
                        feature2count++;

                    } else if (feature2count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest4);
                            distantTerrain.setTag("forest4");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest4nearby);
                            nearbyTerrain.setTag("forest4nearby");

                        }
                        feature2circle.setImageResource(R.drawable.full);
                        feature2count++;

                    } else {
                        feature2count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.forest1);
                            distantTerrain.setTag("forest1");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.forest1nearby);
                            nearbyTerrain.setTag("forest1nearby");

                        }
                        feature2circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain){
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature3count = 0;

                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                    immediateTerrain.setImageResource(R.drawable.pebbles);
                    immediateTerrain.setTag("pebbles");
                    feature2circle.setImageResource(R.drawable.full);
                }
                break;

            //plains1
            //plains1
            //sand
            case R.id.feature3box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain || selectNearbyTerrain) {

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                        nearbyTerrainLayout.setVisibility(View.VISIBLE);
                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature4count = 0;
                    feature5count = 0;
                    feature6count = 0;
                    feature7count = 0;

                    if (feature3count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains1);
                            distantTerrain.setTag("plains1");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains1nearby);
                            nearbyTerrain.setTag("plains1nearby");

                        }

                        feature3circle.setImageResource(R.drawable.quarter);
                        feature3count++;

                    } else if (feature3count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setImageResource(R.drawable.plains2);
                            distantTerrain.setTag("plains2");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains2nearby);
                            nearbyTerrain.setTag("plains2nearby");

                        }

                        feature3circle.setImageResource(R.drawable.half);
                        feature3count++;

                    } else if (feature3count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains3);
                            distantTerrain.setTag("plains3");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains3nearby);
                            nearbyTerrain.setTag("plains3nearby");

                        }
                        feature3circle.setImageResource(R.drawable.threequartercurve);
                        feature3count++;

                    } else if (feature3count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains4);
                            distantTerrain.setTag("plains4");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains4nearby);
                            nearbyTerrain.setTag("plains4nearby");

                        }
                        feature3circle.setImageResource(R.drawable.full);
                        feature3count++;

                    } else {
                        feature3count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.plains1);
                            distantTerrain.setTag("plains1");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.plains1nearby);
                            nearbyTerrain.setTag("plains1nearby");

                        }
                        feature3circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain){
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;

                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                    immediateTerrain.setImageResource(R.drawable.sand);
                    immediateTerrain.setTag("sand");
                    feature3circle.setImageResource(R.drawable.full);
                }
                break;

            //Loch
            //Loch
            //?
            case R.id.feature4box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain || selectNearbyTerrain){

                    if(selectDistantTerrain) {
                        distant.setVisibility(GONE);
                    } else {
                        nearby.setVisibility(GONE);
                        nearbyTerrainLayout.setVisibility(View.VISIBLE);
                    }

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature5count = 0;
                    feature6count = 0;
                    feature7count = 0;

                    if (feature4count == 0) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.loch1);
                            distantTerrain.setTag("loch1");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.loch1nearby);
                            nearbyTerrain.setTag("loch1nearby");

                        }

                        feature4circle.setImageResource(R.drawable.quarter);
                        feature4count++;

                    } else if (feature4count == 1) {
                        if (selectDistantTerrain){
                            distantTerrain.setImageResource(R.drawable.loch2);
                            distantTerrain.setTag("loch2");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.loch2nearby);
                            nearbyTerrain.setTag("loch2nearby");

                        }

                        feature4circle.setImageResource(R.drawable.half);
                        feature4count++;

                    } else if (feature4count == 2) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.loch3);
                            distantTerrain.setTag("loch3");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.loch3nearby);
                            nearbyTerrain.setTag("loch3nearby");

                        }
                        feature4circle.setImageResource(R.drawable.threequartercurve);
                        feature4count++;

                    } else if (feature4count == 3) {
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.loch4);
                            distantTerrain.setTag("loch4");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.loch4nearby);
                            nearbyTerrain.setTag("loch4nearby");

                        }
                        feature4circle.setImageResource(R.drawable.full);
                        feature4count++;

                    } else {
                        feature4count = 1;
                        if(selectDistantTerrain) {
                            distantTerrain.setImageResource(R.drawable.loch1);
                            distantTerrain.setTag("loch1");

                        } else {
                            nearbyTerrain.setImageResource(R.drawable.loch1nearby);
                            nearbyTerrain.setTag("loch1nearby");

                        }
                        feature4circle.setImageResource(R.drawable.quarter);
                    }
                }
                else if(selectImmediateTerrain) {
                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);
                }
                break;

            //Mountains
            //Beach
            case R.id.feature5box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain) {distant.setVisibility(GONE);

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature6count = 0;
                    feature7count = 0;

                    if (feature5count == 0) {
                        distantTerrain.setImageResource(R.drawable.mountains1);
                        distantTerrain.setTag("mountains1");
                        feature5circle.setImageResource(R.drawable.quarter);
                        feature5count++;

                    } else if (feature5count == 1) {
                        distantTerrain.setImageResource(R.drawable.mountains2);
                        distantTerrain.setTag("mountains2");
                        feature5circle.setImageResource(R.drawable.half);
                        feature5count++;

                    } else if (feature5count == 2) {
                        distantTerrain.setImageResource(R.drawable.mountains3);
                        distantTerrain.setTag("mountains3");
                        feature5circle.setImageResource(R.drawable.threequartercurve);
                        feature5count++;

                    } else if (feature5count == 3) {
                        distantTerrain.setImageResource(R.drawable.mountains4);
                        distantTerrain.setTag("mountains4");
                        feature5circle.setImageResource(R.drawable.full);
                        feature5count++;

                    } else {
                        feature5count = 1;
                        distantTerrain.setImageResource(R.drawable.mountains1);
                        distantTerrain.setTag("mountains1");
                        feature5circle.setImageResource(R.drawable.quarter);
                    }

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);
                    nearbyTerrainLayout.setVisibility(View.VISIBLE);

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature6count = 0;
                    feature7count = 0;

                    nearbyTerrain.setImageResource(R.drawable.beach1nearby);
                    nearbyTerrain.setTag("beach1nearby");
                    feature5circle.setImageResource(R.drawable.full);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                }
                break;

            //Valley
            //Cliff
            case R.id.feature6box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain) {distant.setVisibility(GONE);

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;
                    feature7count = 0;

                    if (feature6count == 0) {
                        distantTerrain.setImageResource(R.drawable.valley1);
                        distantTerrain.setTag("valley1");
                        feature6circle.setImageResource(R.drawable.quarter);
                        feature6count++;

                    } else if (feature6count == 1) {
                        distantTerrain.setImageResource(R.drawable.valley2);
                        distantTerrain.setTag("valley2");
                        feature6circle.setImageResource(R.drawable.half);
                        feature6count++;

                    } else if (feature6count == 2) {
                        distantTerrain.setImageResource(R.drawable.valley3);
                        distantTerrain.setTag("valley3");
                        feature6circle.setImageResource(R.drawable.threequartercurve);
                        feature6count++;

                    } else if (feature6count == 3) {
                        distantTerrain.setImageResource(R.drawable.valley4);
                        distantTerrain.setTag("valley4");
                        feature6circle.setImageResource(R.drawable.full);
                        feature6count++;

                    } else {
                        feature6count = 1;
                        distantTerrain.setImageResource(R.drawable.valley1);
                        distantTerrain.setTag("valley1");
                        feature6circle.setImageResource(R.drawable.quarter);
                    }

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);
                    nearbyTerrainLayout.setVisibility(View.VISIBLE);

                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;
                    feature7count = 0;

                    nearbyTerrain.setImageResource(R.drawable.cliff1nearby);
                    nearbyTerrain.setTag("cliff1nearby");
                    feature6circle.setImageResource(R.drawable.full);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                }
                break;

            //Ocean
            case R.id.feature7box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain) {
                    distant.setVisibility(GONE);
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    feature1count = 0;
                    feature2count = 0;
                    feature3count = 0;
                    feature4count = 0;
                    feature5count = 0;
                    feature6count = 0;

                    distantTerrain.setImageResource(R.drawable.ocean1);
                    distantTerrain.setTag("ocean1");
                    feature7circle.setImageResource(R.drawable.full);
                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);
                    nearbyTerrainLayout.setVisibility(View.VISIBLE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.feature8box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain) {

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);
                    nearbyTerrainLayout.setVisibility(View.VISIBLE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.feature9box:

                confirmSelection.setVisibility(View.VISIBLE);

                if(selectDistantTerrain) {

                } else if(selectNearbyTerrain){
                    nearby.setVisibility(GONE);
                    nearbyTerrainLayout.setVisibility(View.VISIBLE);

                } else if(selectImmediateTerrain){
                    immediate.setVisibility(GONE);
                    immediateTerrainLayout.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.confirmSelection:

                if(selectDistantTerrain){

                    selectDistantTerrain = false;
                    selectNearbyTerrain = true;
                    instructions.setText(R.string.nearbyTerrain);
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);
                    //change thumbnail imagesUri?

                    feature1Thumbnail.setImageResource(R.drawable.hillthumbnail);
                    feature2Thumbnail.setImageResource(R.drawable.forestthumbnail);
                    feature3Thumbnail.setImageResource(R.drawable.plainsthumbnail);
                    feature4Thumbnail.setImageResource(R.drawable.lochthumbnail);
                    feature5Thumbnail.setImageResource(R.drawable.beachthumbnail);
                    feature6Thumbnail.setImageResource(R.drawable.cliffthumbnail);
                    feature7box.setVisibility(GONE);
                    feature8box.setVisibility(GONE);
                    feature9box.setVisibility(GONE);
                    confirmSelection.setVisibility(View.GONE);

                    feature1Text.setText(R.string.nearbyFeature1);
                    feature2Text.setText(R.string.nearbyFeature2);
                    feature3Text.setText(R.string.nearbyFeature3);
                    feature4Text.setText(R.string.nearbyFeature4);
                    feature5Text.setText(R.string.nearbyFeature5);
                    feature6Text.setText(R.string.nearbyFeature6);
                    feature7Text.setText(R.string.nearbyFeature7);
                    feature8Text.setText(R.string.nearbyFeature8);
                    feature9Text.setText(R.string.nearbyFeature9);


                    if(distantTerrain.getTag().equals("ocean1")){
                        //no loch
                        feature4dimmer.getForeground().setAlpha(150);
                        feature4box.setClickable(false);
                    } else {
                        //no beach
                        feature5dimmer.getForeground().setAlpha(150);
                        feature5box.setClickable(false);

                        //no cliff
                        feature6dimmer.getForeground().setAlpha(150);
                        feature6box.setClickable(false);
                    }

                } else if (selectNearbyTerrain){
                    selectNearbyTerrain = false;
                    selectImmediateTerrain = true;
                    instructions.setText(R.string.immediateTerrain);
                    resetProgressCircle(feature1circle, feature2circle, feature3circle, feature4circle, feature5circle, feature6circle, feature7circle);

                    feature1Thumbnail.setImageResource(R.drawable.grassthumbnail);
                    feature2Thumbnail.setImageResource(R.drawable.pebblesthumbnail);
                    feature3Thumbnail.setImageResource(R.drawable.sandthumbnail);
                    feature4box.setVisibility(GONE);
                    feature5box.setVisibility(GONE);
                    feature6box.setVisibility(GONE);
                    feature7box.setVisibility(GONE);
                    feature8box.setVisibility(GONE);
                    feature9box.setVisibility(GONE);
                    confirmSelection.setText("Complete");
                    confirmSelection.setVisibility(View.GONE);

                    feature1Text.setText(R.string.immediateFeature1);
                    feature2Text.setText(R.string.immediateFeature2);
                    feature3Text.setText(R.string.immediateFeature3);
                    feature4Text.setText(R.string.immediateFeature4);
                    feature5Text.setText(R.string.immediateFeature5);
                    feature6Text.setText(R.string.immediateFeature6);
                    feature7Text.setText(R.string.immediateFeature7);
                    feature8Text.setText(R.string.immediateFeature8);
                    feature9Text.setText(R.string.immediateFeature9);

                } else if(selectImmediateTerrain){


                    Intent i = new Intent(getApplicationContext(), AddSiteActivity.class);

                    System.out.println("distant terrain: " + distantTerrain.getTag().toString());
                    System.out.println("nearby terrain: " + nearbyTerrain.getTag().toString());
                    System.out.println("immediate terrain: " + immediateTerrain.getTag().toString());

                    //bundle other site data
                    i.putExtra("image", image);
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("title", title);
                    i.putExtra("description", description);
                    i.putExtra("rating", rating);

                    //bundle the various selections.
                    i.putExtra("distantTerrain", distantTerrain.getTag().toString());
                    i.putExtra("nearbyTerrain", nearbyTerrain.getTag().toString());
                    i.putExtra("immediateTerrain", immediateTerrain.getTag().toString());

                    startActivity(i);
                    finish();
                }


                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                Intent intent;
                if(update){
                    intent = new Intent(getApplicationContext(), AddSiteActivity.class);

                    //bundle for update
                    intent.putExtra("arrayPosition", arrayPos);
                } else {
                    intent = new Intent(getApplicationContext(), AddSiteActivity.class);

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
