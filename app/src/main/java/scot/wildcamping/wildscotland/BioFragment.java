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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BioFragment extends Fragment {

    public BioFragment() {
    }

    TextView txtName;
    TextView txtEmail;
    TextView txtBio;
    TextView txtWhy;
    Boolean this_user = false;
    CircleImageView profile_pic;
    ImageView cover_pic;
    ImageButton edit;

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
        txtWhy = (TextView) rootView.findViewById(R.id.why);
        txtBio = (TextView) rootView.findViewById(R.id.bio);
        profile_pic = (CircleImageView) rootView.findViewById(R.id.profilePicture);
        cover_pic = (ImageView) rootView.findViewById(R.id.backgroundImage);
        edit = (ImageButton) rootView.findViewById(R.id.edit);

        session = new SessionManager(getContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        if(this_user){


            txtName.setText(AppController.getString(getContext(), "name"));
            txtEmail.setText(AppController.getString(getContext(), "email"));
            String bio = AppController.getString(getContext(), "bio");
            String why = AppController.getString(getContext(), "why");
            String profile_pic = AppController.getString(getContext(), "profile_pic");
            String cover_pic = AppController.getString(getContext(), "cover_pic");

            if(bio.equals("null")){
                txtBio.setText("");
            } else {
                txtBio.setText(bio);
            }

            if(why.equals("null")){
                txtWhy.setText("");
            } else {
                txtWhy.setText(why);
            }

            if(!profile_pic.equals("null") || !profile_pic.equals("")){
                Bitmap bit = StringToBitMap(profile_pic);
                this.profile_pic.setImageBitmap(bit);
            }

            if(!cover_pic.equals("null") || !cover_pic.equals("")){
                Bitmap bit = StringToBitMap(cover_pic);
                this.cover_pic.setImageBitmap(bit);
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

            edit.setVisibility(View.GONE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
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

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
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


