package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import scot.wildcamping.wildscotland.Adapters.CustomSpinnerAdapter;
import scot.wildcamping.wildscotland.Adapters.ViewPagerAdapter;
import scot.wildcamping.wildscotland.AsyncTask.FetchQuestions;

/**
 *
 * Created by Chris on 08-Apr-16.
 */
public class ProfileActivity extends AppCompatActivity {

    private Spinner spinner_nav;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Questions", "Badges"};
    int Numboftabs =2;
    ArrayList<String> list;
    int currPosition;
    boolean initialSelection = false;
    String user;
    int progressValue;

    TextView txtName;
    TextView txtEmail;
    TextView txtBio;
    TextView txtWhy;
    Boolean this_user = false;
    CircleImageView profile_pic;
    ImageView cover_pic;
    ProgressBar progress;
    TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            this_user = extras.getBoolean("this_user");
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        user = AppController.getString(this, "user");

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtWhy = (TextView) findViewById(R.id.why);
        txtBio = (TextView) findViewById(R.id.bio);
        profile_pic = (CircleImageView) findViewById(R.id.profilePicture);
        cover_pic = (ImageView) findViewById(R.id.backgroundImage);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        RelativeLayout progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);

        if(this_user){

            txtName.setText(AppController.getString(this, "name"));
            txtEmail.setText(AppController.getString(this, "email"));
            String bio = AppController.getString(this, "bio");
            String why = AppController.getString(this, "why");
            String profile_pic = AppController.getString(this, "profile_pic");
            String cover_pic = AppController.getString(this, "cover_pic");

            if(bio.equals("null")){
                txtBio.setText("");
            } else {
                txtBio.setText(bio);
                updateProgress();
            }

            if(why.equals("null")){
                txtWhy.setText("");
            } else {
                txtWhy.setText(why);
                updateProgress();
            }

            if(!profile_pic.equals("null") || !profile_pic.equals("")){
                Bitmap bit = StringToBitMap(profile_pic);
                this.profile_pic.setImageBitmap(bit);
                updateProgress();
            }

            if(!cover_pic.equals("null") || !cover_pic.equals("")){
                Bitmap bit = StringToBitMap(cover_pic);
                this.cover_pic.setImageBitmap(bit);
                updateProgress();
            }



        } else {
            txtName.setText(AppController.getString(this, "user_name"));
            txtEmail.setText(AppController.getString(this, "user_email"));

            String bio = AppController.getString(this, "user_bio");

            if(bio.equals("null")){
                txtBio.setText("");
            } else {
                txtBio.setText(bio);
            }

            String image = AppController.getString(this, "user_profile_pic");

            if(!image.equals("null") || !image.equals("")){
                Bitmap bit = StringToBitMap(image);
                profile_pic.setImageBitmap(bit);
                updateProgress();
            }

            progress.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
        }

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateProfileActivity.class);
                intent.putExtra("update", true);
                startActivity(intent);
            }
        });

        addItemsToSpinner();
    }

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        list = new ArrayList<String>();
        list.add("Map");
        list.add("SitesActivity");
        list.add("TradesActivity");
        list.add("Profile");

        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);



        // Default ArrayAdapter with default spinner item layout, getting some
        // view rendering problem in lollypop device, need to test in other
        // devices

		/*
		 * ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_spinner_item, list);
		 * spinAdapter.setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item);
		 */

        spinner_nav.setAdapter(spinAdapter);
        spinner_nav.setSelection(3);
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                if(initialSelection){
                    displayView(position);
                }
                initialSelection = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                Intent i = new Intent(getApplicationContext(), MainActivity_Spinner.class);
                i.putExtra("data", false);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
                break;
            case 1:
                Intent intent = new Intent(getApplicationContext(), SitesActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;

            case 2:
                Intent trade = new Intent(getApplicationContext(), TradesActivity.class);
                startActivity(trade);
                overridePendingTransition(0,0);
                finish();
                break;

            case 3:
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                profile.putExtra("this_user", true);
                startActivity(profile);
                overridePendingTransition(0,0);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings1) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_search1) {
            Toast.makeText(getApplicationContext(), "Search Clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_add1) {
            Toast.makeText(getApplicationContext(), "Add Clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_delete1) {
            Toast.makeText(getApplicationContext(), "Delete Clicked",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_refresh) {
            if(isNetworkAvailable()) {
                try {
                    String questions = new FetchQuestions(this, AppController.getString(this, "email")).execute().get();
                } catch (InterruptedException e) {

                } catch (ExecutionException e) {

                }
            }
            displayView(3);
        } else if(id == R.id.action_tradeHistory){
            Intent intent = new Intent(getApplicationContext(), TradeHistoryActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public Bitmap getCroppedBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public void updateProgress(){

        progressValue = progress.getProgress()+20;
        progress.setProgress(progressValue);
        progressText.setText(progressValue+"% Complete");

    }
}
