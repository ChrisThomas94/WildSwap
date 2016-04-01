package scot.wildcamping.wildscotland.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import scot.wildcamping.wildscotland.R;
import scot.wildcamping.wildscotland.model.Site;

/**
 * Created by Chris on 18-Mar-16.
 */
public class SiteListAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<Site> knownSites;

    public SiteListAdapter(Context context, SparseArray<Site> knownSites){
        this.context = context;
        this.knownSites = knownSites;
    }

    @Override
    public int getCount() {
        return knownSites.size();
    }

    @Override
    public Object getItem(int position) {
        return knownSites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.known_sites_list, null);
        }

        RelativeLayout site = (RelativeLayout) convertView.findViewById(R.id.site);
        //ImageView siteThumbnail1 = (ImageView) convertView.findViewById(R.id.siteThumbnail1);
        //ImageView siteThumbnail2 = (ImageView) convertView.findViewById(R.id.siteThumbnail2);
        //ImageView siteThumbnail3 = (ImageView) convertView.findViewById(R.id.siteThumbnail3);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView placeholderFeatures = (TextView) convertView.findViewById(R.id.placeholderFeatures);
        ImageView feature1 = (ImageView) convertView.findViewById(R.id.preview_feature1);
        ImageView feature2 = (ImageView) convertView.findViewById(R.id.preview_feature2);
        ImageView feature3 = (ImageView) convertView.findViewById(R.id.preview_feature3);
        ImageView feature4 = (ImageView) convertView.findViewById(R.id.preview_feature4);
        ImageView feature5 = (ImageView) convertView.findViewById(R.id.preview_feature5);
        ImageView feature6 = (ImageView) convertView.findViewById(R.id.preview_feature6);
        ImageView feature7 = (ImageView) convertView.findViewById(R.id.preview_feature7);
        ImageView feature8 = (ImageView) convertView.findViewById(R.id.preview_feature8);
        ImageView feature9 = (ImageView) convertView.findViewById(R.id.preview_feature9);
        ImageView feature10 = (ImageView) convertView.findViewById(R.id.preview_feature10);
        RatingBar rating = (RatingBar) convertView.findViewById(R.id.ratingThumbnail);
        TextView ratedBy = (TextView) convertView.findViewById(R.id.ratedBy);

        //siteThumbnail.setImageResource(knownSites.get(position).getImage());
        title.setText(knownSites.get(position).getTitle());


        /*if(knownSites.get(position).getImage() != null){
            Bitmap image = StringToBitMap(knownSites.get(position).getImage());
            siteThumbnail1.setVisibility(View.VISIBLE);
            siteThumbnail1.setImageBitmap(image);
        }
        if(knownSites.get(position).getImage2() != null){
            Bitmap image = StringToBitMap(knownSites.get(position).getImage2());
            siteThumbnail2.setVisibility(View.VISIBLE);
            siteThumbnail2.setImageBitmap(image);
        }
        if(knownSites.get(position).getImage3() != null){
            Bitmap image = StringToBitMap(knownSites.get(position).getImage3());
            siteThumbnail3.setVisibility(View.VISIBLE);
            siteThumbnail3.setImageBitmap(image);
        }*/


        if(knownSites.get(position).getFeature1().equals("0")){
            feature1.setVisibility(View.GONE);
        } else {
            feature1.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature2().equals("0")){
            feature2.setVisibility(View.GONE);
        } else {
            feature2.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature3().equals("0")){
            feature3.setVisibility(View.GONE);
        } else {
            feature3.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature4().equals("0")){
            feature4.setVisibility(View.GONE);
        } else {
            feature4.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature5().equals("0")){
            feature5.setVisibility(View.GONE);
        } else {
            feature5.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature6().equals("0")){
            feature6.setVisibility(View.GONE);
        } else {
            feature6.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature7().equals("0")){
            feature7.setVisibility(View.GONE);
        } else {
            feature7.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature8().equals("0")){
            feature8.setVisibility(View.GONE);
        } else {
            feature8.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature9().equals("0")){
            feature9.setVisibility(View.GONE);
        } else {
            feature9.setVisibility(View.VISIBLE);
        }
        if(knownSites.get(position).getFeature10().equals("0")){
            feature10.setVisibility(View.GONE);
        } else {
            feature10.setVisibility(View.VISIBLE);
        }

        if (knownSites.get(position).getFeature1().equals("0") && knownSites.get(position).getFeature2().equals("0") && knownSites.get(position).getFeature3().equals("0") && knownSites.get(position).getFeature4().equals("0") && knownSites.get(position).getFeature5().equals("0") && knownSites.get(position).getFeature6().equals("0") && knownSites.get(position).getFeature7().equals("0") && knownSites.get(position).getFeature8().equals("0") && knownSites.get(position).getFeature9().equals("0") && knownSites.get(position).getFeature10().equals("0")){
            placeholderFeatures.setVisibility(View.VISIBLE);
        } else {
            placeholderFeatures.setVisibility(View.INVISIBLE);
        }

        rating.setRating(knownSites.get(position).getRating().floatValue());
        ratedBy.setText("Rated By: " + knownSites.get(position).getRatedBy());

        return convertView;
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
}
