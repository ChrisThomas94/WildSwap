package scot.wildcamping.wildscotland.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import scot.wildcamping.wildscotland.R;


/**
 * Created by Chris on 19-Mar-17.
 */

public class ImageGalleryAdapter extends PagerAdapter {

    private Context context;
    ArrayList<String> data = new ArrayList();
    ImageButton imageButton;
    LayoutInflater mLayoutInflater;

    public ImageGalleryAdapter(Context context, ArrayList<String> data) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.adapter_image, container, false);

        Log.d("Image Gallery Adapter", "instantiate item");

        Bitmap imageBit = StringToBitMap(data.get(position));

        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
        imageButton.setId(position);
        imageButton.setLayoutParams(new LinearLayout.LayoutParams(900, 900));
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setClickable(true);
        imageButton.setImageBitmap(imageBit);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
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


