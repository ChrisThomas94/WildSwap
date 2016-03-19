package scot.wildcamping.wildscotland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            cid = extras.getString("cid");
            titleBun = extras.getString("title");
            descriptionBun = extras.getString("description");
            ratingBun = extras.getDouble("rating");
            feature1 = extras.getString("feature1");
            feature2 = extras.getString("feature2");
            feature3 = extras.getString("feature3");
            feature4 = extras.getString("feature4");
            feature5 = extras.getString("feature5");
            feature6 = extras.getString("feature6");
            feature7 = extras.getString("feature7");
            feature8 = extras.getString("feature8");
            feature9 = extras.getString("feature9");
            feature10 = extras.getString("feature10");

            title.setText(titleBun);
            description.setText(descriptionBun);
            rating.setRating(ratingBun.floatValue());

            //1 = true
            //0 = false

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

            System.out.println(feature1);
            System.out.println(feature2);
            System.out.println(feature3);
            System.out.println(feature4);
            System.out.println(feature5);
            System.out.println(feature6);
            System.out.println(feature7);
            System.out.println(feature8);
            System.out.println(feature9);
            System.out.println(feature10);
        }

        contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.contactSiteAdmin:
                Intent i = new Intent(getApplicationContext(), ActivityContactUser.class);
                startActivity(i);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("fragment", 2);
                startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
