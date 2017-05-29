package scot.wildcamping.wildswap.Adapters;

import android.app.Activity;
import android.content.Context;
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

import scot.wildcamping.wildswap.R;
import scot.wildcamping.wildswap.Objects.Trade;

/**
 * Created by Chris on 18-Mar-16.
 *
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
        TextView tradeLocation = (TextView)convertView.findViewById(R.id.tradeLocation);

        System.out.println("size" + activeTrades.size());
        System.out.println("position" + position);

        System.out.println("trade" + activeTrades.get(position));

        String direction = activeTrades.get(position).getUserRelation();
        int status = activeTrades.get(position).getStatus();
        String location = activeTrades.get(position).getLocation();

        title.setText(direction);
        tradeLocation.setText(location);
        System.out.println("direction" + direction);

        if(status == 1){
            outcomeTrade.setText(R.string.rejected);
            trade.setBackgroundResource(R.drawable.rounded_linear_layout_red);
        } else if (status == 2){
            outcomeTrade.setText(R.string.accepted);
            trade.setBackgroundResource(R.drawable.rounded_linear_layout_green);
        } else if (status == 0){
            outcomeTrade.setText(R.string.pending);
        }


        //http://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getDefault());

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
