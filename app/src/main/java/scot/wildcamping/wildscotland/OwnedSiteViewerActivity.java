package scot.wildcamping.wildscotland;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.model.Gallery;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

/**
 * Created by Chris on 26-Feb-16.
 */
public class OwnedSiteViewerActivity extends AppCompatActivity implements View.OnClickListener {

    int arrayPos;
    Double latitude;
    Double longitude;
    String cid;
    String titleBun;
    String descriptionBun;
    Double ratingBun;
    SparseArray<String> features;
    Boolean feature1 = true;
    Boolean feature2 = true;
    Boolean feature3 = true;
    Boolean feature4 = true;
    Boolean feature5 = true;
    Boolean feature6 = true;
    Boolean feature7 = true;
    Boolean feature8 = true;
    Boolean feature9 = true;
    Boolean feature10 = true;
    String imageStr;
    Bitmap imageBit;
    int prevState;
    SparseArray<Site> owned = new SparseArray<>();
    int RESULT_LOAD_IMAGE = 0;
    SparseArray<Gallery> images = new SparseArray<>();
    Gallery gallery;
    FrameLayout frame;
    ImageButton image1;
    ImageButton image2;
    ImageButton image3;
    ImageView close;
    ScrollView scroll;

    ImageView expandedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_site_viewer);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Button back = (Button)findViewById(R.id.deactivateSite);
        Button edit = (Button)findViewById(R.id.editSite);
        TextView lat = (TextView)findViewById(R.id.lat);
        TextView lon = (TextView)findViewById(R.id.lon);
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
        image1 = (ImageButton)findViewById(R.id.image1);
        image2 = (ImageButton)findViewById(R.id.image2);
        image3 = (ImageButton)findViewById(R.id.image3);
        ImageView distantTerrainFeatures = (ImageView)findViewById(R.id.distantTerrainFeatures);
        ImageView nearbyTerrainFeatures = (ImageView)findViewById(R.id.nearbyTerrainFeatures);
        ImageView immediateTerrainFeatures = (ImageView)findViewById(R.id.immediateTerrainFeatures);
        TextView ratedBy = (TextView)findViewById(R.id.ratedBy);
        LinearLayout featuresBackground = (LinearLayout)findViewById(R.id.featuresBackground);
        LinearLayout imageContainer = (LinearLayout)findViewById(R.id.imagesLinear);
        expandedImage = (ImageView)findViewById(R.id.expanded_image);
        frame = (FrameLayout) findViewById(R.id.frame);
        close = (ImageView)findViewById(R.id.closeImage);
        scroll = (ScrollView)findViewById(R.id.scroll);

        frame.getForeground().setAlpha(0);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            arrayPos = extras.getInt("arrayPosition");
            cid = extras.getString("cid");
            prevState = extras.getInt("prevState");

            try{
                String images = new FetchSiteImages(this,cid).execute().get();

            } catch (InterruptedException e) {

            }catch (ExecutionException e) {

            }
        }

        knownSite inst = new knownSite();
        owned = inst.getOwnedSitesMap();

        knownSite im = new knownSite();
        images = im.getImages();

        Site focused = owned.get(arrayPos);

        cid = focused.getCid();
        String id = cid.substring(cid.length()-8);
        int cidEnd = Integer.parseInt(id);
        gallery = images.get(cidEnd);

        latitude = focused.getLat();
        longitude = focused.getLon();

        lat.setText("Latitude: "+latitude.toString());
        lon.setText("Longitude: "+longitude.toString());

        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        rating.setRating((focused.getRating()).floatValue());
        ratedBy.setText("Rated By: " + focused.getRatedBy());

        if(gallery.getImage1() != null) {
            if (gallery.getImage1().equals("null")) {
                image1.setVisibility(View.GONE);
                imageContainer.setVisibility(View.GONE);

            } else {
                imageBit = StringToBitMap(gallery.getImage1());
                image1.setImageBitmap(imageBit);
                image1.setVisibility(View.VISIBLE);
                image1.setOnClickListener(this);
            }
        } else {
            image1.setVisibility(View.GONE);
            imageContainer.setVisibility(View.GONE);
        }

        if(gallery.getImage2() != null) {
            if (gallery.getImage2().equals("null")) {
                image2.setVisibility(View.GONE);
            } else {
                imageBit = StringToBitMap(gallery.getImage2());
                image2.setImageBitmap(imageBit);
                image2.setVisibility(View.VISIBLE);
                image2.setOnClickListener(this);
            }
        }else {
            image2.setVisibility(View.GONE);
        }

        if(gallery.getImage3() != null) {
            if (gallery.getImage3().equals("null")) {
                image3.setVisibility(View.GONE);
            } else {
                imageBit = StringToBitMap(gallery.getImage3());
                image3.setImageBitmap(imageBit);
                image3.setVisibility(View.VISIBLE);
                image3.setOnClickListener(this);
            }
        }else {
            image3.setVisibility(View.GONE);
        }

        if(focused.getDistant().equals("null") && focused.getNearby().equals("null") && focused.getImmediate().equals("null")) {
            distantTerrainFeatures.setVisibility(View.GONE);
            nearbyTerrainFeatures.setVisibility(View.GONE);
            immediateTerrainFeatures.setVisibility(View.GONE);

        } else if(!focused.getDistant().equals("") && !focused.getNearby().equals("") && !focused.getImmediate().equals("")){
            int distantID = this.getResources().getIdentifier("drawable/"+ focused.getDistant(), null, this.getPackageName());
            int nearbyID = this.getResources().getIdentifier("drawable/"+ focused.getNearby(), null, this.getPackageName());
            int immediateID = this.getResources().getIdentifier("drawable/"+ focused.getImmediate(), null, this.getPackageName());

            distantTerrainFeatures.setImageResource(distantID);
            nearbyTerrainFeatures.setImageResource(nearbyID);
            immediateTerrainFeatures.setImageResource(immediateID);

            distantTerrainFeatures.setVisibility(View.VISIBLE);
            nearbyTerrainFeatures.setVisibility(View.VISIBLE);
            immediateTerrainFeatures.setVisibility(View.VISIBLE);

        } else {
            distantTerrainFeatures.setVisibility(View.GONE);
            nearbyTerrainFeatures.setVisibility(View.GONE);
            immediateTerrainFeatures.setVisibility(View.GONE);
        }

        /*features = focused.getFeatures();
        for (int i = 0; i<features.size(); i++){
            if(features.get(i+1).equals("0")){
                String featureID = "feature"+(i+1)+"Image";

                feature1Image.setVisibility(View.GONE);
                feature1 = false;
            }
        }*/

        if(focused.getFeature1().equals("0")){
            feature1Image.setVisibility(View.GONE);
            feature1 = false;
        }
        if(focused.getFeature2().equals("0")){
            feature2Image.setVisibility(View.GONE);
            feature2 = false;
        }
        if(focused.getFeature3().equals("0")){
            feature3Image.setVisibility(View.GONE);
            feature3 = false;
        }
        if(focused.getFeature4().equals("0")){
            feature4Image.setVisibility(View.GONE);
            feature4 = false;
        }
        if(focused.getFeature5().equals("0")){
            feature5Image.setVisibility(View.GONE);
            feature5 = false;
        }
        if(focused.getFeature6().equals("0")){
            feature6Image.setVisibility(View.GONE);
            feature6 = false;
        }
        if(focused.getFeature7().equals("0")){
            feature7Image.setVisibility(View.GONE);
            feature7 = false;
        }
        if(focused.getFeature8().equals("0")){
            feature8Image.setVisibility(View.GONE);
            feature8 = false;
        }
        if(focused.getFeature9().equals("0")){
            feature9Image.setVisibility(View.GONE);
            feature9 = false;
        }
        if(focused.getFeature10().equals("0")){
            feature10Image.setVisibility(View.GONE);
            feature10 = false;
        }

        if(!feature1 && !feature2 && !feature3 && !feature4 && !feature5 && !feature6 && !feature7 && !feature8 && !feature9 && !feature10){
            featuresBackground.setVisibility(View.GONE);
        }

        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        image1.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.deactivateSite:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Attention!");
                builder1.setMessage("Are you sure you want to delete this site?");

                builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        boolean active = false;
                        //String ratingReq = Double.toString(ratingBun);
                        Intent intent = new Intent(getApplicationContext(), MainActivity_Spinner.class);
                        //trigger php to deactivate site
                        new UpdateSite(getApplicationContext(), true, active, cid, null, null, null, null, null, null, null, null, null, null, null, null, null, imageStr, 0).execute();
                        startActivity(intent);
                        finish();


                    }

                });

                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert1 = builder1.create();
                alert1.show();

                break;

            case R.id.editSite:
                intent = new Intent(getApplicationContext(),UpdateSiteViewerActivity.class);
                //bundle all current details into "add site"
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

                startActivity(intent);
                finish();
                break;

            case R.id.image1:

                imageBit = StringToBitMap(gallery.getImage1());
                expandedImage.setImageBitmap(imageBit);
                expandedImage.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                frame.getForeground().setAlpha(150);

                break;

            case R.id.image2:

                imageBit = StringToBitMap(gallery.getImage2());
                expandedImage.setImageBitmap(imageBit);
                expandedImage.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                frame.getForeground().setAlpha(150);

                break;

            case R.id.image3:

                imageBit = StringToBitMap(gallery.getImage3());
                expandedImage.setImageBitmap(imageBit);
                expandedImage.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                frame.getForeground().setAlpha(150);

                break;

            case R.id.closeImage:

                expandedImage.setVisibility(View.INVISIBLE);
                close.setVisibility(View.GONE);
                frame.getForeground().setAlpha(0);
                scroll.setClickable(true);
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

                //Intent intent = null;
                if(prevState == 1) {
                    //intent = new Intent(getApplicationContext(),Sites.class);
                    //intent.putExtra("fragment", 1);
                    //startActivity(intent);
                    //finish();
                } else {
                    finish();
                }
                return true;

            case R.id.showOnMap:
                Intent intent = new Intent(this, MainActivity_Spinner.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("add", true);
                startActivity(intent);
                finish();

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.site_viewer, menu);
        return true;
    }

    public String getStringImage(Bitmap bmp){
        if(bmp != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return null;
        }
    }

}
