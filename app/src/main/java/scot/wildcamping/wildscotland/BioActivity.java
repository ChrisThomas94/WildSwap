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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import scot.wildcamping.wildscotland.adapter.QuestionListAdapter;
import scot.wildcamping.wildscotland.model.Image;
import scot.wildcamping.wildscotland.model.Question;
import scot.wildcamping.wildscotland.model.Quiz;

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
    ImageView prof;
    String image;
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
        prof = (ImageView) findViewById(R.id.profilePicture);
        TextView skip = (TextView)findViewById(R.id.skip);

        if(!isNew){

            if(AppController.getString(this, "bio").equals("null")){
                bio.setText("");
            } else {
                bio.setText(AppController.getString(this, "bio"));
            }

            image = AppController.getString(this, "profile_pic");

            if(image != null){

                if(image.equals("null")){
                    Snackbar.make(parentLayout, "Why not add a profile picture?", Snackbar.LENGTH_LONG).show();

                } else {
                    Bitmap profile_pic = StringToBitMap(image);
                    Bitmap circle = getCroppedBitmap(profile_pic);
                    prof.setImageBitmap(circle);
                }

            }

        }

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

                if(image != null) {
                    imageSingleLine = image.replaceAll("[\r\n]+", "");
                    AppController.setString(this, "profile_pic", image);
                } else {
                    AppController.setString(this, "profile_pic", "null");
                }

                //asynk task updating bio
                try {
                    String update = new UpdateProfile(this, newBio, imageSingleLine).execute().get();
                } catch (InterruptedException e){

                } catch(ExecutionException e){

                }

                if(isNetworkAvailable()) {
                    try {
                        String questions = new FetchQuestions(this, AppController.getString(this, "email")).execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {

                    }
                }

                Intent intent = new Intent(this, QuizActivity.class);

                if(update){
                    intent.putExtra("update", true);

                } else if (isNew){
                    intent.putExtra("new", true);

                }
                startActivity(intent);
                finish();
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

                compress = Bitmap.createScaledBitmap(compress, 300, 300, true);
                image = getStringImage(compress);

                Bitmap circle = getCroppedBitmap(compress);
                prof.setImageBitmap(circle);


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
