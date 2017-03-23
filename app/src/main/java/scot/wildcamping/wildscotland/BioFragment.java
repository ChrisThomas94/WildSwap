package scot.wildcamping.wildscotland;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BioFragment extends Fragment {

    public BioFragment() {
    }

    TextView txtName;
    TextView txtEmail;
    TextView txtBio;
    Button btnLogout;
    TextView update;
    Boolean this_user = false;
    ImageView profile_pic;

    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bio, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            this_user = extras.getBoolean("this_user");
        }

        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);
        txtBio = (TextView) rootView.findViewById(R.id.bio);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        update = (TextView) rootView.findViewById(R.id.updateProfile);
        profile_pic = (ImageView) rootView.findViewById(R.id.profilePicture);

        session = new SessionManager(getContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }



        //User user = new User();

        //String name = user.getName();
        //String email = user.getEmail();
        //String bio = user.getBio();

        //txtName.setText(name);
        //txtEmail.setText(email);
        //txtBio.setText(bio);

        if(this_user){
            txtName.setText(AppController.getString(getContext(), "name"));
            txtEmail.setText(AppController.getString(getContext(), "email"));

            String bio = AppController.getString(getContext(), "bio");

            if(bio.equals("null")){
                txtBio.setText("");
            } else {
                txtBio.setText(bio);
            }

            String image = AppController.getString(getContext(), "profile_pic");

            if(image.equals("null") || image.equals("")){

            } else {
                Bitmap bit = StringToBitMap(image);
                bit = Bitmap.createScaledBitmap(bit, 300, 300, true);
                Bitmap circle = getCroppedBitmap(bit);
                profile_pic.setImageBitmap(circle);
            }

        } else {
            txtName.setText(AppController.getString(getContext(), "user_name"));
            txtEmail.setText(AppController.getString(getContext(), "user_email"));

            String bio = AppController.getString(getContext(), "user_bio");

            if(bio.equals("null")){
                txtBio.setText("");
            } else {
                txtBio.setText(bio);
            }

            String image = AppController.getString(getContext(), "user_profile_pic");

            if(image.equals("null") || image.equals("")){

            } else {
                Bitmap bit = StringToBitMap(image);
                bit = Bitmap.createScaledBitmap(bit, 300, 300, true);

                Bitmap circle = getCroppedBitmap(bit);
                profile_pic.setImageBitmap(circle);
            }

            update.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),
                        BioActivity.class);
                intent.putExtra("update", true);
                startActivity(intent);
            }
        });

        return rootView;
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                goToAttract(v);
        }
    }*/
/*
    public void goToAttract(View v){
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }*/

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                //Intent intent = new Intent(getApplicationContext(),MainActivity_Spinner.class);
                //startActivity(intent);
                getActivity().finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
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
}


