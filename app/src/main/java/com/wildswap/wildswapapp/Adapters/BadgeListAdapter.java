package com.wildswap.wildswapapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.wildswap.wildswapapp.Objects.Badge;
import com.wildswap.wildswapapp.R;

/**
 * Created by Chris on 18-Mar-16.
 *
 */
public class BadgeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Integer> badges;
    private SparseArray<Badge> allBadges;

    public BadgeListAdapter(Context context, ArrayList<Integer> badges, SparseArray<Badge> allBadges){
        this.context = context;
        this.badges = badges;
        this.allBadges = allBadges;

    }

    private static final class ViewHolder{
        private ImageView badgeThumbnail;
        private TextView badgeTitle;
        private TextView badgeDesc;
    }

    @Override
    public int getCount() {
        return allBadges.size();
    }

    @Override
    public Object getItem(int position) {
        return allBadges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_list_badges, null);
            viewHolder = new ViewHolder();

            viewHolder.badgeThumbnail = (ImageView) convertView.findViewById(R.id.badgeThumbnail);
            viewHolder.badgeTitle = (TextView) convertView.findViewById(R.id.badgeTitle);
            viewHolder.badgeDesc = (TextView) convertView.findViewById(R.id.badgeDesc);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(allBadges.get(position) != null) {
            viewHolder.badgeTitle.setText(allBadges.get(position).getTitle());
            viewHolder.badgeDesc.setText(allBadges.get(position).getDescription());

            if(badges.get(position)!=0){
                viewHolder.badgeThumbnail.setImageResource(allBadges.get(position).getResource());

            }
        }

        return convertView;
    }

}
