package com.wildswap.wildswapapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.UpdateProfile;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

/**
 * Created by Chris on 09-Apr-16.
 *
 */
public class CreateProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    StoredData inst = new StoredData();
    User thisUser = inst.getLoggedInUser();

    boolean update = false;
    int RESULT_LOAD_IMAGE = 0;
    int progressValue = 0;
    Boolean updateUserType = true;
    Boolean uploadImage = true;
    Boolean uploadCover = true;
    Boolean updateBio = true;
    Boolean updateWhy = true;
    Boolean anyUpdates = false;

    TextView name;
    EditText bio;
    EditText why;
    CircleImageView profilePic;
    ImageView addCoverPicture;
    ImageView coverPic;
    RelativeLayout userType1;
    RelativeLayout userType2;
    RelativeLayout userType3;
    TextView userTypeText1;
    TextView userTypeText2;
    TextView userTypeText3;
    TextView userTypeDescription;
    ProgressBar progress;
    TextView progressText;
    RelativeLayout progressLayout;

    String profilePicString;
    String coverPicString;
    String userType;
    String oldBio;
    String oldWhy;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        View parentLayout = findViewById(android.R.id.content);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            update = extras.getBoolean("update");
        }

        name = (TextView) findViewById(R.id.name);
        bio = (EditText) findViewById(R.id.bio);
        why = (EditText) findViewById(R.id.why);
        profilePic = (CircleImageView) findViewById(R.id.profilePicture);
        addCoverPicture = (ImageView) findViewById(R.id.coverPicture);
        coverPic = (ImageView)findViewById(R.id.backgroundImage);
        userType1 = (RelativeLayout)findViewById(R.id.userType1);
        userType2 = (RelativeLayout)findViewById(R.id.userType2);
        userType3 = (RelativeLayout)findViewById(R.id.userType3);
        userTypeText1 = (TextView) findViewById(R.id.userTypeText1);
        userTypeText2 = (TextView) findViewById(R.id.userTypeText2);
        userTypeText3 = (TextView) findViewById(R.id.userTypeText3);
        userTypeDescription = (TextView) findViewById(R.id.userTypeDescription);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);

        //name.setText(AppController.getString(this, "name"));
        name.setText(thisUser.getName());

        if(update){

            getSupportActionBar().setTitle("Update Profile");

            profilePicString = thisUser.getProfile_pic();
            coverPicString = thisUser.getCover_pic();

            if(profilePicString != null){

                if(profilePicString.equals("null")){
                    Snackbar.make(parentLayout, "Why not add a profile picture?", Snackbar.LENGTH_LONG).show();

                } else {
                    Bitmap profile_pic = StringToBitMap(profilePicString);
                    Bitmap circle = getCroppedBitmap(profile_pic);
                    profilePic.setImageBitmap(circle);
                    updateProgress();
                }
            }

            if(coverPicString != null){
                if(coverPicString.equals("null")){

                } else {
                    Bitmap cover_pic = StringToBitMap(coverPicString);
                    coverPic.setImageBitmap(cover_pic);
                    updateProgress();
                    addCoverPicture.setVisibility(View.INVISIBLE);
                }
            }


            if(thisUser.getUserType().equals(getResources().getString(R.string.userType1))){
                userType = userTypeText1.getText().toString();
                userType1.setBackgroundResource(R.drawable.rounded_green_button);
                userTypeDescription.setText(R.string.userTypeDescription1);
                updateProgress();
                updateUserType = false;

            } else if (thisUser.getUserType().equals(getResources().getString(R.string.userType2))){
                userType = userTypeText2.getText().toString();
                userType2.setBackgroundResource(R.drawable.rounded_green_button);
                userTypeDescription.setText(R.string.userTypeDescription2);
                updateProgress();
                updateUserType = false;

            } else if (thisUser.getUserType().equals(getResources().getString(R.string.userType3))){
                userType = userTypeText3.getText().toString();
                userType3.setBackgroundResource(R.drawable.rounded_green_button);
                userTypeDescription.setText(R.string.userTypeDescription3);
                updateProgress();
                updateUserType = false;

            }


            if(thisUser.getBio().equals("null")){
                bio.setText("");
                oldBio = "";
            } else {
                oldBio = thisUser.getBio();
                bio.setText(oldBio);
                updateProgress();
            }

            if(thisUser.getWhy().equals("null")){
                why.setText("");
                oldWhy = "";
            } else {
                oldWhy = thisUser.getWhy();
                why.setText(oldWhy);
                updateProgress();
            }

        } else {

            ab.setDisplayHomeAsUpEnabled(false);
            ab.setHomeButtonEnabled(false);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Thanks for Registering!");
            builder1.setMessage(getResources().getString(R.string.welcomeText));
            builder1.setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert1 = builder1.create();
            alert1.show();
        }

        if(!update){
            userType = userTypeText1.getText().toString();
            userType1.setBackgroundResource(R.drawable.rounded_green_button);
            userTypeDescription.setText(R.string.userTypeDescription1);
            updateProgress();
            updateUserType = false;
        }

        addCoverPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra("imageType", "cover");
                addCoverPicture.setVisibility(View.GONE);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        coverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra("imageType", "cover");
                addCoverPicture.setVisibility(View.GONE);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                anyUpdates = true;

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra("imageType", "profile");
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                anyUpdates = true;

            }
        });

        //setting onclick listeners
        bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(updateBio) {
                    updateProgress();
                }
                updateBio = false;
            }
        });

        why.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(updateWhy) {
                    updateProgress();
                }
                updateWhy = false;
            }
        });

        userType1.setOnClickListener(this);
        userType2.setOnClickListener(this);
        userType3.setOnClickListener(this);
        progress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.userType1:

                if(updateUserType){
                    updateProgress();
                }

                updateUserType = false;
                anyUpdates = true;

                userType = userTypeText1.getText().toString();
                userType1.setBackgroundResource(R.drawable.rounded_green_button);
                userType2.setBackgroundResource(R.drawable.rounded_white_button);
                userType3.setBackgroundResource(R.drawable.rounded_white_button);
                userTypeDescription.setText(R.string.userTypeDescription1);
                break;

            case R.id.userType2:

                if(updateUserType){
                    updateProgress();
                }

                updateUserType = false;
                anyUpdates = true;

                userType = userTypeText2.getText().toString();
                userType1.setBackgroundResource(R.drawable.rounded_white_button);
                userType2.setBackgroundResource(R.drawable.rounded_green_button);
                userType3.setBackgroundResource(R.drawable.rounded_white_button);
                userTypeDescription.setText(R.string.userTypeDescription2);
                break;

            case R.id.userType3:

                if(updateUserType){
                    updateProgress();
                }

                updateUserType = false;
                anyUpdates = true;

                userType = userTypeText3.getText().toString();
                userType1.setBackgroundResource(R.drawable.rounded_white_button);
                userType2.setBackgroundResource(R.drawable.rounded_white_button);
                userType3.setBackgroundResource(R.drawable.rounded_green_button);
                userTypeDescription.setText(R.string.userTypeDescription3);
                break;

            case R.id.progressBar:

                submit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //Intent intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        String imageType;

        if(resultCode == RESULT_OK){
            Uri targetUri = data.getData();

            try{
                Bitmap compress = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                try {
                    ExifInterface ei = new ExifInterface(targetUri.getPath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            compress = rotateImage(compress, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            compress = rotateImage(compress, 180);
                            break;
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                imageType = i.getStringExtra("imageType");

                if(imageType.equals("cover")){

                    if(uploadCover){
                        updateProgress();
                    }

                    uploadCover = false;

                    coverPicString = getStringImage(compress);
                    coverPic.setImageBitmap(compress);

                } else if(imageType.equals("profile")){

                    if(uploadImage){
                        updateProgress();
                    }

                    uploadImage = false;

                    profilePicString = getStringImage(compress);
                    profilePic.setImageBitmap(compress);

                } else {

                }

            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
    }

    public void submit(){
        String newBio = bio.getText().toString();

        if(newBio.isEmpty()){
            newBio = "null";
        }

        String newWhy = why.getText().toString();

        if(newWhy.isEmpty()){
            newWhy = "null";
        }

        String profileSingleLine = null;
        String coverSingleLine = null;
        String bioSingleLine = newBio.replaceAll("[\r\n]+", "");
        String whySingleLine = newWhy.replaceAll("[\r\n]+", "");

        thisUser.setBio(bioSingleLine);
        thisUser.setWhy(whySingleLine);

        if(profilePicString != null) {
            profileSingleLine = profilePicString.replaceAll("[\r\n]+", "");
            thisUser.setProfile_pic(profilePicString);
        }

        if(coverPicString != null){
            coverSingleLine = coverPicString.replaceAll("[\r\n]+", "");
            thisUser.setCover_pic(coverPicString);
        }

        final Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("update", update);

        //if any changes have been made
        if(anyUpdates || !newBio.equals(oldBio) || !newWhy.equals(oldWhy)) {

            if (isNetworkAvailable()) {

                String token = AppController.getString(this, "token");

                //asynk task updating bio
                new UpdateProfile(this, userType, bioSingleLine, whySingleLine, profileSingleLine, coverSingleLine, update, token, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {

                        if(output.equals("Success")) {
                            startActivity(intent);
                            finish();
                        }
                    }
                }).execute();
            } else {
                //show no network error!
                Log.d("Error", "There is no network");
                startActivity(intent);
                finish();
            }
        } else {
            startActivity(intent);
            finish();
        }
    }

    public String getStringImage(Bitmap bmp){
        if(bmp != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return null;
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public void updateProgress(){

        progressValue = progress.getProgress()+20;
        progress.setProgress(progressValue);

        if(progressValue >= 100){
            progressText.setText("GO");
        } else {
            progressText.setText(progressValue + "% Complete");
        }

    }

}
