package com.wildswap.wildswapapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import com.wildswap.wildswapapp.Objects.Site;
import com.wildswap.wildswapapp.Objects.User;
import com.wildswap.wildswapapp.R;


/**
 * Created by Chris on 19-Mar-17.
 *
 */

public class TradeUnknownSitesAdapter extends PagerAdapter {

    private Context context;
    private SparseArray<Site> unknownSites = new SparseArray<>();
    private LayoutInflater mLayoutInflater;
    private SparseArray<User> someUsers;
    Geocoder geocoder;

    public TradeUnknownSitesAdapter(Context context, SparseArray<Site> unknownSites, SparseArray<User> someUsers) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.unknownSites = unknownSites;
        this.someUsers = someUsers;

        geocoder = new Geocoder(context, Locale.getDefault());
    }

    @Override
    public int getCount() {
        return unknownSites.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        System.out.println("unknown position " + position);

        View itemView = mLayoutInflater.inflate(R.layout.adapter_unknown_sites_trade, container, false);

        Site thisSite = unknownSites.get(position);
        User dealer = new User();
        String admin = thisSite.getSiteAdmin();

        for(int i = 0; i<someUsers.size();i++){
            dealer = someUsers.get(i);
            if(admin.equals(dealer.getEmail())){
                break;
            }
        }

        if(!dealer.equals(null)) {
            System.out.println("site " + thisSite);
            FrameLayout classLayout = (FrameLayout) itemView.findViewById(R.id.classifications);
            ImageView backgroundImage = (ImageView) itemView.findViewById(R.id.backgroundImage);
            CircleImageView profile_pic = (CircleImageView) itemView.findViewById(R.id.profilePicture);
            TextView title = (TextView) itemView.findViewById(R.id.recieveTitle);
            TextView amateur = (TextView) itemView.findViewById(R.id.classificationA);
            TextView casual = (TextView) itemView.findViewById(R.id.classificationC);
            TextView expert = (TextView) itemView.findViewById(R.id.classificationE);
            RatingBar rating = (RatingBar) itemView.findViewById(R.id.recieveRating);
            TextView ratedBy = (TextView) itemView.findViewById(R.id.ratedBy);
            TextView country = (TextView) itemView.findViewById(R.id.country);

            LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

            System.out.println("user " + dealer);
            String profile_picString = dealer.getProfile_pic();
            String cover_picString = dealer.getCover_pic();
            List<android.location.Address> address = null;
            LatLng pos = thisSite.getPosition();

            try {
                address = geocoder.getFromLocation(pos.latitude, pos.longitude, 1);
            } catch (IOException e) {

            }

            if(!address.isEmpty()) {
                Address thisAddress = address.get(0);
                if(thisAddress.getLocality() == null || thisAddress.getLocality().equals("null")){
                    country.setText(thisAddress.getCountryName());
                } else {
                    country.setText(thisAddress.getCountryName() + ", " + thisAddress.getLocality());
                }
            }

            title.setText(thisSite.getTitle());
            rating.setRating(thisSite.getRating().floatValue());
            ratedBy.setText("Rated By: " + thisSite.getRatedBy());
            String amateurText = amateur.getText().toString();
            String casualText = casual.getText().toString();
            String expertText = expert.getText().toString();

            amateur.setVisibility(View.INVISIBLE);
            casual.setVisibility(View.INVISIBLE);
            expert.setVisibility(View.INVISIBLE);



            if (!thisSite.getClassification().isEmpty()) {

                if (thisSite.getClassification().equals(amateurText)) {
                    amateur.setVisibility(View.VISIBLE);
                    classLayout.setBackgroundResource(R.drawable.rounded_green_button);

                } else if (thisSite.getClassification().equals(casualText)) {
                    casual.setVisibility(View.VISIBLE);
                    classLayout.setBackgroundResource(R.drawable.rounded_orange_button);

                } else if (thisSite.getClassification().equals(expertText)) {
                    expert.setVisibility(View.VISIBLE);
                    classLayout.setBackgroundResource(R.drawable.rounded_red_button);

                }
            }

            if (profile_picString != null && !profile_picString.equals("null") && !profile_picString.equals("")) {
                Bitmap bit = StringToBitMap(profile_picString);
                profile_pic.setImageBitmap(bit);
            }

            if (cover_picString != null && !cover_picString.equals("null") && !cover_picString.equals("")) {
                Bitmap bit = StringToBitMap(cover_picString);
                backgroundImage.setImageBitmap(bit);
            }

            container.addView(itemView);
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}


