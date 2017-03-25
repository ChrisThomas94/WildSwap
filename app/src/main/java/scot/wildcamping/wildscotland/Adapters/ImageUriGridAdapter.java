package scot.wildcamping.wildscotland.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import scot.wildcamping.wildscotland.R;

/**
 * Created by Chris on 18-Mar-16.
 */
public class ImageUriGridAdapter extends ArrayAdapter {

    private Context context;
    //private String[] gallery;
    int layoutResourceId;
    ArrayList data = new ArrayList();
    ImageView imageView;

    public ImageUriGridAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            imageView = (ImageView) row.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


            try {
                String image = data.get(position).toString();
                Uri myUri = Uri.parse(image);
                Bitmap compress = BitmapFactory.decodeStream(row.getContext().getContentResolver().openInputStream(myUri));

                imageView.setImageBitmap(compress);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            row.getTag();
        }


        /*if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.grid_item_layout, null);
        }*/

        return row;
    }

}
