package scot.wildcamping.wildscotland.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.StoredData;
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.R;

/**
 * Created by Chris on 11-April-17.
 *
 */

public class TradeOwnedSitesAdapter extends PagerAdapter {

    private Context context;
    private SparseArray<Site> ownedSites = new SparseArray<>();
    private LayoutInflater mLayoutInflater;
    StoredData inst;
    User thisUser;

    public TradeOwnedSitesAdapter(Context context, SparseArray<Site> ownedSites) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.ownedSites = ownedSites;
        inst = new StoredData();
        thisUser = inst.getLoggedInUser();
    }

    @Override
    public int getCount() {
        return ownedSites.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        System.out.println("owned position " + position);

        View itemView = mLayoutInflater.inflate(R.layout.adapter_owned_sites_trade, container, false);

        Site thisSite = ownedSites.get(position);

        ImageView siteThumbnail = (ImageView) itemView.findViewById(R.id.image);
        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView amateur = (TextView) itemView.findViewById(R.id.classificationA);
        TextView casual = (TextView) itemView.findViewById(R.id.classificationC);
        TextView expert = (TextView) itemView.findViewById(R.id.classificationE);
        TextView country = (TextView) itemView.findViewById(R.id.country);
        CircleImageView profile_pic = (CircleImageView) itemView.findViewById(R.id.profilePicture);

        String profile_picString = thisUser.getProfile_pic();

        List<Address> address = thisSite.getAddress();
        Address thisAddress = address.get(0);

        title.setText(thisSite.getTitle());
        country.setText(thisAddress.getCountryName() + ", " + thisAddress.getLocality());

        String amateurText = amateur.getText().toString();
        String casualText = casual.getText().toString();
        String expertText = expert.getText().toString();

        amateur.setVisibility(View.INVISIBLE);
        casual.setVisibility(View.INVISIBLE);
        expert.setVisibility(View.INVISIBLE);

        if(thisSite.getClassification() != null) {

            if (thisSite.getClassification().equals(amateurText)) {
                amateur.setVisibility(View.VISIBLE);

            } else if (thisSite.getClassification().equals(casualText)) {
                casual.setVisibility(View.VISIBLE);

            } else if (thisSite.getClassification().equals(expertText)) {
                expert.setVisibility(View.VISIBLE);

            }
        }

        if(thisSite.getDisplay_pic() != null){
            String image = thisSite.getDisplay_pic();
            Bitmap imageBit = StringToBitMap(image);
            siteThumbnail.setImageBitmap(imageBit);
        }

        if(!profile_picString.equals("null") || !profile_picString.equals("")){
            Bitmap bit = StringToBitMap(profile_picString);
            profile_pic.setImageBitmap(bit);
        }

        container.addView(itemView);

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


