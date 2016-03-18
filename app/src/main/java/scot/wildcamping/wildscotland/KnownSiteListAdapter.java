package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import scot.wildcamping.wildscotland.slidingmenu.model.NavDrawerItem;

/**
 * Created by Chris on 18-Mar-16.
 */
public class KnownSiteListAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<Site> knownSites;

    public KnownSiteListAdapter(Context context, SparseArray<Site> knownSites){
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
            convertView = mInflater.inflate(R.layout.list_known_sites, null);
        }

        RelativeLayout site = (RelativeLayout) convertView.findViewById(R.id.site);
        ImageView siteThumbnail = (ImageView) convertView.findViewById(R.id.siteThumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
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

        //siteThumbnail.setImageResource(knownSites.get(position).getImage());
        title.setText(knownSites.get(position).getTitle());

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

        rating.setRating(knownSites.get(position).getRating().floatValue());

        return convertView;
    }
}
