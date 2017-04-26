package scot.wildcamping.wildscotland.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import scot.wildcamping.wildscotland.Objects.Question;
import scot.wildcamping.wildscotland.Objects.Quiz;
import scot.wildcamping.wildscotland.R;

/**
 * Created by Chris on 18-Mar-16.
 *
 */
public class BadgeListAdapter extends BaseAdapter {

    private Context context;
    private SparseBooleanArray collection;
    private SparseIntArray badges;

    public BadgeListAdapter(Context context, SparseBooleanArray collection, SparseIntArray badges){
        this.context = context;
        this.collection = collection;
        this.badges = badges;
    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public Object getItem(int position) {
        return collection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_list_badges, null);
        }

        ImageView badgeThumbnail = (ImageView) convertView.findViewById(R.id.badgeThumbnail);
        TextView badgeTitle = (TextView) convertView.findViewById(R.id.badgeTitle);
        TextView badgeDesc = (TextView) convertView.findViewById(R.id.badgeDesc);

        badgeThumbnail.setImageResource(badges.get(position));


        return convertView;
    }

}
