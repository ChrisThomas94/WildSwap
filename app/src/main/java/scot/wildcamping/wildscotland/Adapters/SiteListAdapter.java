package scot.wildcamping.wildscotland.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import scot.wildcamping.wildscotland.R;
import scot.wildcamping.wildscotland.Objects.Site;

/**
 * Created by Chris on 18-Mar-16.
 *
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
            convertView = mInflater.inflate(R.layout.adapter_known_sites_list, null);
        }

        RelativeLayout site = (RelativeLayout) convertView.findViewById(R.id.site);
        ImageView siteThumbnail = (ImageView) convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView amateur = (TextView) convertView.findViewById(R.id.classificationA);
        TextView casual = (TextView) convertView.findViewById(R.id.classificationC);
        TextView expert = (TextView) convertView.findViewById(R.id.classificationE);
        TextView country = (TextView) convertView.findViewById(R.id.country);

        List<Address> address = knownSites.get(position).getAddress();
        Address thisAddress = address.get(0);

        title.setText(knownSites.get(position).getTitle());
        country.setText(thisAddress.getCountryName() + ", " + thisAddress.getLocality());

        String amateurText = amateur.getText().toString();
        String casualText = casual.getText().toString();
        String expertText = expert.getText().toString();

        amateur.setVisibility(View.INVISIBLE);
        casual.setVisibility(View.INVISIBLE);
        expert.setVisibility(View.INVISIBLE);

        if(knownSites.get(position).getClassification() == null){

        } else if(knownSites.get(position).getClassification().equals(amateurText)){
            amateur.setVisibility(View.VISIBLE);

        } else if(knownSites.get(position).getClassification().equals(casualText)){
            casual.setVisibility(View.VISIBLE);

        } else if(knownSites.get(position).getClassification().equals(expertText)){
            expert.setVisibility(View.VISIBLE);

        } else {

        }

        if(knownSites.get(position).getDisplay_pic() == null){

        } else {
            String image = knownSites.get(position).getDisplay_pic();
            Bitmap imageBit = StringToBitMap(image);
            siteThumbnail.setImageBitmap(imageBit);
        }



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
