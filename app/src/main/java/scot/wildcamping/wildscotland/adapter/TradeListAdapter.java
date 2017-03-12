package scot.wildcamping.wildscotland.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import scot.wildcamping.wildscotland.R;
import scot.wildcamping.wildscotland.model.Trade;

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
            convertView = mInflater.inflate(R.layout.adapter_list_trades, null);
        }

        RelativeLayout trade = (RelativeLayout) convertView.findViewById(R.id.trade);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView titleTrade = (TextView)convertView.findViewById(R.id.titleTrade);
        TextView outcomeTrade = (TextView)convertView.findViewById(R.id.outcomeTrade);

        String direction = activeTrades.get(position).getUserRelation();
        int status = activeTrades.get(position).getStatus();

        title.setText(direction);
        //titleTrade.setText(activeTrades.get(position).getDate());

        if(status == 1){
            outcomeTrade.setText("- Rejected");
            trade.setBackgroundResource(R.drawable.rounded_linear_layout_red);
        } else if (status == 2){
            outcomeTrade.setText("- Accepted");
            trade.setBackgroundResource(R.drawable.rounded_linear_layout_green);
        } else if (status == 0){
            outcomeTrade.setText("- Pending");
        }


        //http://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
        Date date = null;
        String dtStart = "2010-10-15T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("London")); //"Europe/London"
        try {
            date = format.parse(activeTrades.get(position).getDate());
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        titleTrade.setText(android.text.format.DateUtils.getRelativeTimeSpanString(date.getTime()));


        return convertView;
    }
}
