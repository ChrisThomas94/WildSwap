package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import scot.wildcamping.wildscotland.model.Image;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

/**
 * Created by Chris on 26-Feb-16.
 */
public class OwnedSiteActivity extends AppCompatActivity implements View.OnClickListener {

    int arrayPos;
    Double latitude;
    Double longitude;
    String cid;
    String titleBun;
    String descriptionBun;
    Double ratingBun;
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
    ImageView addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_site);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Button back = (Button)findViewById(R.id.deactivateSite);
        Button edit = (Button)findViewById(R.id.editSite);
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
        ImageView image1 = (ImageView)findViewById(R.id.image1);
        ImageView image2 = (ImageView)findViewById(R.id.image2);
        ImageView image3 = (ImageView)findViewById(R.id.image3);
        addImage = (ImageView)findViewById(R.id.addImage);
        TextView ratedBy = (TextView)findViewById(R.id.ratedBy);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            arrayPos = extras.getInt("arrayPosition");
            cid = extras.getString("cid");
            prevState = extras.getInt("prevState");
        }

        knownSite inst = new knownSite();
        owned = inst.getOwnedSitesMap();

        Site focused = owned.get(arrayPos);

        title.setText(focused.getTitle());
        description.setText(focused.getDescription());
        rating.setRating((focused.getRating()).floatValue());
        ratedBy.setText("Rated By: " + focused.getRatedBy());

        if(focused.getImage() != null) {
            imageBit = StringToBitMap(focused.getImage());
            image1.setImageBitmap(imageBit);
            image1.setVisibility(View.VISIBLE);
        }
        if(focused.getImage2() != null) {
            imageBit = StringToBitMap(focused.getImage2());
            image2.setImageBitmap(imageBit);
            image2.setVisibility(View.VISIBLE);
        }
        if(focused.getImage3() != null) {
            imageBit = StringToBitMap(focused.getImage3());
            image3.setImageBitmap(imageBit);
            image3.setVisibility(View.VISIBLE);
        }

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

        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        image1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.deactivateSite:
                boolean active = false;
                //String ratingReq = Double.toString(ratingBun);
                intent = new Intent(getApplicationContext(),MainActivity.class);
                //trigger php to deactivate site
                new UpdateSite(this, true, active, cid, null, null, null, null, null, null, null, null, null, null, null, null, null, imageStr).execute();
                startActivity(intent);
                finish();
                break;

            case R.id.editSite:
                intent = new Intent(getApplicationContext(),UpdateSiteActivity.class);
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
                if(prevState == 1) {
                    intent.putExtra("fragment", 1);
                }
                startActivity(intent);
                finish();
                return true;

            case R.id.addImage:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            try{
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                String image = getStringImage(bitmap);

                addImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
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
