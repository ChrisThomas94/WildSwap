package scot.wildcamping.wildscotland.slidingmenu.adapter;

import android.app.Activity;
import android.content.Context;
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
import scot.wildcamping.wildscotland.Site;
import scot.wildcamping.wildscotland.Trade;

/**
 * Created by Chris on 18-Mar-16.
 */
public class TradeListAdapter extends BaseAdapter{

    private Context context;
    private SparseArray<Trade> activeTrades;

    public TradeListAdapter(Context context, SparseArray<Trade> activeTrades){
        this.context = context;
        this.activeTrades = activeTrades;
    }

    @Override
    public int getCount() {
        return activeTrades.size();
    }

    @Override
    public Object getItem(int position) {
        return activeTrades.get(position);
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
            convertView = mInflater.inflate(R.layout.fragment_open_trades_list, null);
        }

        RelativeLayout trade = (RelativeLayout) convertView.findViewById(R.id.trade);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView titleTrade = (TextView)convertView.findViewById(R.id.titleTrade);

        title.setText(activeTrades.get(position).getUserRelation());
        titleTrade.setText(activeTrades.get(position).getDate());

        return convertView;
    }
}
