package scot.wildcamping.wildscotland.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.media.Rating;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.User;
import scot.wildcamping.wildscotland.R;


/**
 * Created by Chris on 19-Mar-17.
 *
 */

public class TradeUnknownSitesAdapter extends PagerAdapter {

    private Context context;
    private SparseArray<Site> unknownSites = new SparseArray<>();
    private LayoutInflater mLayoutInflater;
    private User dealer;

    public TradeUnknownSitesAdapter(Context context, SparseArray<Site> unknownSites, SparseArray<User> dealer) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.unknownSites = unknownSites;
        this.dealer = dealer;
    }

    @Override
    public int getCount() {
        return unknownSites.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.adapter_unknown_sites_trade, container, false);

        Site thisSite = unknownSites.get(position);
        ImageView backgroundImage = (ImageView) itemView.findViewById(R.id.backgroundImage);
        CircleImageView profile_pic = (CircleImageView) itemView.findViewById(R.id.profilePicture);
        TextView title = (TextView) itemView.findViewById(R.id.recieveTitle);
        TextView amateur = (TextView) itemView.findViewById(R.id.classificationA);
        TextView casual = (TextView) itemView.findViewById(R.id.classificationC);
        TextView expert = (TextView) itemView.findViewById(R.id.classificationE);
        RatingBar rating = (RatingBar) itemView.findViewById(R.id.recieveRating);
        TextView country = (TextView) itemView.findViewById(R.id.country);

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

        if(!profile_picString.equals("null") || !profile_picString.equals("")){
            Bitmap bit = StringToBitMap(profile_picString);
            profile_pic.setImageBitmap(bit);
        }



        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}


