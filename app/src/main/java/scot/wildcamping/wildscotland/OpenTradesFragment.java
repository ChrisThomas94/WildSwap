package scot.wildcamping.wildscotland;

import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OpenTradesFragment extends Fragment implements View.OnClickListener{
	
	public OpenTradesFragment(){}

    RelativeLayout trade1;
    RelativeLayout trade2;
    RelativeLayout trade3;
    RelativeLayout trade4;
    RelativeLayout trade5;
    RelativeLayout trade6;
    RelativeLayout trade7;
    RelativeLayout trade8;
    RelativeLayout trade9;
    RelativeLayout trade10;

    TextView titleTrade1;
    TextView titleTrade2;
    TextView titleTrade3;
    TextView titleTrade4;
    TextView titleTrade5;
    TextView titleTrade6;
    TextView titleTrade7;
    TextView titleTrade8;
    TextView titleTrade9;
    TextView titleTrade10;

    SparseArray<Trade> activeTrades = new SparseArray<>();
    private int tradesSize;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_open_trades, container, false);      //fragment_open_trades

        trade1 = (RelativeLayout)rootView.findViewById(R.id.trade1);
        trade2 = (RelativeLayout)rootView.findViewById(R.id.trade2);
        trade3 = (RelativeLayout)rootView.findViewById(R.id.trade3);
        trade4 = (RelativeLayout)rootView.findViewById(R.id.trade4);
        trade5 = (RelativeLayout)rootView.findViewById(R.id.trade5);
        trade6 = (RelativeLayout)rootView.findViewById(R.id.trade6);
        trade7 = (RelativeLayout)rootView.findViewById(R.id.trade7);
        trade8 = (RelativeLayout)rootView.findViewById(R.id.trade8);
        trade9 = (RelativeLayout)rootView.findViewById(R.id.trade9);
        trade10 = (RelativeLayout)rootView.findViewById(R.id.trade10);

        titleTrade1 = (TextView)rootView.findViewById(R.id.titleTrade1);
        titleTrade2 = (TextView)rootView.findViewById(R.id.titleTrade2);
        titleTrade3 = (TextView)rootView.findViewById(R.id.titleTrade3);
        titleTrade4 = (TextView)rootView.findViewById(R.id.titleTrade4);
        titleTrade5 = (TextView)rootView.findViewById(R.id.titleTrade5);
        titleTrade6 = (TextView)rootView.findViewById(R.id.titleTrade6);
        titleTrade7 = (TextView)rootView.findViewById(R.id.titleTrade7);
        titleTrade8 = (TextView)rootView.findViewById(R.id.titleTrade8);
        titleTrade9 = (TextView)rootView.findViewById(R.id.titleTrade9);
        titleTrade10 = (TextView)rootView.findViewById(R.id.titleTrade10);

        StoredTrades trades = new StoredTrades();
        activeTrades = trades.getActiveTrades();
        tradesSize = trades.getActiveTradesSize();

        if(tradesSize <1){

        }
        if(tradesSize >= 1){
            titleTrade1.setText(activeTrades.get(0).getDate());
            trade1.setVisibility(View.VISIBLE);
        }
        if (tradesSize >= 2){
            titleTrade2.setText(activeTrades.get(1).getDate());
            trade2.setVisibility(View.VISIBLE);
        }
        if (tradesSize >= 3){
            titleTrade3.setText(activeTrades.get(2).getDate());
            trade3.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 4){
            titleTrade4.setText(activeTrades.get(3).getDate());
            trade4.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 5){
            titleTrade5.setText(activeTrades.get(4).getDate());
            trade5.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 6){
            titleTrade6.setText(activeTrades.get(5).getDate());
            trade6.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 7){
            titleTrade7.setText(activeTrades.get(6).getDate());
            trade7.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 8){
            titleTrade8.setText(activeTrades.get(7).getDate());
            trade8.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 9){
            titleTrade9.setText(activeTrades.get(8).getDate());
            trade9.setVisibility(View.VISIBLE);
        }
        if(tradesSize >= 10){
            titleTrade10.setText(activeTrades.get(9).getDate());
            trade10.setVisibility(View.VISIBLE);
        }

        trade1.setOnClickListener(this);
        trade2.setOnClickListener(this);
        trade3.setOnClickListener(this);
        trade4.setOnClickListener(this);
        trade5.setOnClickListener(this);
        trade6.setOnClickListener(this);
        trade7.setOnClickListener(this);
        trade8.setOnClickListener(this);
        trade9.setOnClickListener(this);
        trade10.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.trade1:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(0).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(0).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(0).getRecieve_cid());
                startActivity(intent);

                break;

            case R.id.trade2:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(1).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(1).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(1).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade3:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(2).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(2).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(2).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade4:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(3).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(3).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(3).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade5:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(4).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(4).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(4).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade6:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(5).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(5).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(5).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade7:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(6).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(6).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(6).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade8:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(7).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(7).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(7).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade9:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(8).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(8).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(8).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade10:
                intent = new Intent(getActivity(),
                        TradeView.class);
                intent.putExtra("unique_tid", activeTrades.get(9).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(9).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(9).getRecieve_cid());
                startActivity(intent);
                break;
        }

    }
}
