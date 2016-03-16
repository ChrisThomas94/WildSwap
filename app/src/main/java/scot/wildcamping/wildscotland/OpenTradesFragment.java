package scot.wildcamping.wildscotland;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OpenTradesFragment extends Fragment implements View.OnClickListener{
	
	public OpenTradesFragment(){}

    final String sent = "Sent";
    final String received = "Received";

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

    TextView title1;
    TextView title2;
    TextView title3;
    TextView title4;
    TextView title5;
    TextView title6;
    TextView title7;
    TextView title8;
    TextView title9;
    TextView title10;

    private int tradesSize;
    SparseArray<Trade> activeTrades = new SparseArray<>();

    //private int sentTradesSize;
    //SparseArray<Trade> sentTrades = new SparseArray<>();

   // private int receivedTradesSize;
    //SparseArray<Trade> receivedTrades = new SparseArray<>();

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

        title1 = (TextView)rootView.findViewById(R.id.title1);
        title2 = (TextView)rootView.findViewById(R.id.title2);
        title3 = (TextView)rootView.findViewById(R.id.title3);
        title4 = (TextView)rootView.findViewById(R.id.title4);
        title5 = (TextView)rootView.findViewById(R.id.title5);
        title6 = (TextView)rootView.findViewById(R.id.title6);
        title7 = (TextView)rootView.findViewById(R.id.title7);
        title8 = (TextView)rootView.findViewById(R.id.title8);
        title9 = (TextView)rootView.findViewById(R.id.title9);
        title10 = (TextView)rootView.findViewById(R.id.title10);

        StoredTrades trades = new StoredTrades();

        activeTrades = trades.getActiveTrades();
        tradesSize = trades.getActiveTradesSize();

        //sentTrades = trades.getSentTrades();
        //sentTradesSize = trades.getSentTradesSize();

        //receivedTrades = trades.getReceivedTrades();
        //receivedTradesSize = trades.getReceivedTradesSize();

        //if no open trades then this displays the text of the last open trade minor problem
        if(tradesSize >= 1){
            titleTrade1.setText(activeTrades.get(0).getDate());
            trade1.setVisibility(View.VISIBLE);
            title1.setText(activeTrades.get(0).getUserRelation());
        }
        if (tradesSize >= 2){
            titleTrade2.setText(activeTrades.get(1).getDate());
            trade2.setVisibility(View.VISIBLE);
            title2.setText(activeTrades.get(1).getUserRelation());
        }
        if (tradesSize >= 3){
            titleTrade3.setText(activeTrades.get(2).getDate());
            trade3.setVisibility(View.VISIBLE);
            title3.setText(activeTrades.get(2).getUserRelation());
        }
        if(tradesSize >= 4){
            titleTrade4.setText(activeTrades.get(3).getDate());
            trade4.setVisibility(View.VISIBLE);
            title4.setText(activeTrades.get(3).getUserRelation());
        }
        if(tradesSize >= 5){
            titleTrade5.setText(activeTrades.get(4).getDate());
            trade5.setVisibility(View.VISIBLE);
            title5.setText(activeTrades.get(4).getUserRelation());
        }
        if(tradesSize >= 6){
            titleTrade6.setText(activeTrades.get(5).getDate());
            trade6.setVisibility(View.VISIBLE);
            title6.setText(activeTrades.get(5).getUserRelation());
        }
        if(tradesSize >= 7){
            titleTrade7.setText(activeTrades.get(6).getDate());
            trade7.setVisibility(View.VISIBLE);
            title7.setText(activeTrades.get(6).getUserRelation());
        }
        if(tradesSize >= 8){
            titleTrade8.setText(activeTrades.get(7).getDate());
            trade8.setVisibility(View.VISIBLE);
            title8.setText(activeTrades.get(7).getUserRelation());
        }
        if(tradesSize >= 9){
            titleTrade9.setText(activeTrades.get(8).getDate());
            trade9.setVisibility(View.VISIBLE);
            title9.setText(activeTrades.get(8).getUserRelation());
        }
        if(tradesSize >= 10){
            titleTrade10.setText(activeTrades.get(9).getDate());
            trade10.setVisibility(View.VISIBLE);
            title10.setText(activeTrades.get(9).getUserRelation());
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
                if(activeTrades.get(0).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(0).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(0).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(0).getRecieve_cid());
                startActivity(intent);

                break;

            case R.id.trade2:
                if(activeTrades.get(1).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(1).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(1).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(1).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade3:
                if(activeTrades.get(2).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(2).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(2).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(2).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade4:
                if(activeTrades.get(3).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(3).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(3).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(3).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade5:
                if(activeTrades.get(4).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(4).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(4).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(4).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade6:
                if(activeTrades.get(5).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(5).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(5).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(5).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade7:
                if(activeTrades.get(6).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(6).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(6).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(6).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade8:
                if(activeTrades.get(7).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(7).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(7).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(7).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade9:
                if(activeTrades.get(8).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(8).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(8).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(8).getRecieve_cid());
                startActivity(intent);
                break;

            case R.id.trade10:
                if(activeTrades.get(9).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(9).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(9).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(9).getRecieve_cid());
                startActivity(intent);
                break;
        }

    }
}
