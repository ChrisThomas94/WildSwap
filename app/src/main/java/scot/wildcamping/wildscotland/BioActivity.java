package scot.wildcamping.wildscotland;

import android.content.Context;
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
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import scot.wildcamping.wildscotland.Adapters.QuestionListAdapter;
import scot.wildcamping.wildscotland.AsyncTask.AsyncResponse;
import scot.wildcamping.wildscotland.AsyncTask.FetchQuestions;
import scot.wildcamping.wildscotland.AsyncTask.UpdateProfile;
import scot.wildcamping.wildscotland.Objects.Question;
import scot.wildcamping.wildscotland.Objects.Quiz;

/**
 * Created by Chris on 09-Apr-16.
 */
public class BioActivity extends AppCompatActivity {

    private QuestionListAdapter adapter;
    private ListView mDrawerList;
    Quiz inst;
    SparseArray<Question> question;
    boolean update = false;
    int RESULT_LOAD_IMAGE = 0;

    EditText bio;
    CircleImageView prof;
    ImageView addCoverPicture;
    String profilePic;
    ImageView backgroundImage;
    Intent i;
    boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        View parentLayout = findViewById(android.R.id.content);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            update = extras.getBoolean("update");
            isNew = extras.getBoolean("new");
        }

        bio = (EditText) findViewById(R.id.bio);
        prof = (CircleImageView) findViewById(R.id.profilePicture);
        TextView skip = (TextView)findViewById(R.id.skip);
        addCoverPicture = (ImageView) findViewById(R.id.coverPicture);
        backgroundImage = (ImageView)findViewById(R.id.backgroundImage);

        if(!isNew){

            skip.setVisibility(View.VISIBLE);

            if(AppController.getString(this, "bio").equals("null")){
                bio.setText("");
            } else {
                bio.setText(AppController.getString(this, "bio"));
            }

            profilePic = AppController.getString(this, "profile_pic");

            if(profilePic != null){

                if(profilePic.equals("null")){
                    Snackbar.make(parentLayout, "Why not add a profile picture?", Snackbar.LENGTH_LONG).show();

                } else {
                    Bitmap profile_pic = StringToBitMap(profilePic);
                    Bitmap circle = getCroppedBitmap(profile_pic);
                    prof.setImageBitmap(circle);
                }

            }

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

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.putExtra("imageType", "profile");
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                if(isNew){
                    intent.putExtra("new", true);
                } else if (update) {
                    intent.putExtra("update", true);
                }
                startActivity(intent);
                finish();
            }
        });

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

            case R.id.action_submit:

                AppController.setString(this, "bio", bio.getText().toString());

                String newBio = bio.getText().toString();

                String imageSingleLine = null;

                if(profilePic != null) {
                    imageSingleLine = profilePic.replaceAll("[\r\n]+", "");
                    AppController.setString(this, "profile_pic", profilePic);
                } else {
                    AppController.setString(this, "profile_pic", "null");
                }

                final Intent intent = new Intent(this, QuizActivity.class);

                if(update){
                    intent.putExtra("update", true);

                } else if (isNew){
                    intent.putExtra("new", true);
                }

                if(isNetworkAvailable()) {

                    new FetchQuestions(this, AppController.getString(this, "email")).execute();

                    //asynk task updating bio
                    new UpdateProfile(this, newBio, imageSingleLine, new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            startActivity(intent);
                            finish();
                        }
                    }).execute();
                } else {
                    //show no network error!
                    Log.d("Error","There is no network");
                    startActivity(intent);
                    finish();
                }
                break;
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
                    //compress = Bitmap.createScaledBitmap(compress, 300, 300, true);
                    //profilePic = getStringImage(compress);

                    //Bitmap square = getCroppedBitmap(compress);
                    backgroundImage.setImageBitmap(compress);

                } else if(imageType.equals("profile")){

                    prof.setImageBitmap(compress);

                } else {

                }




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

}
