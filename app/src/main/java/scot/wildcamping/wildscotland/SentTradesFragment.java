package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import scot.wildcamping.wildscotland.adapter.TradeListAdapter;
import scot.wildcamping.wildscotland.model.StoredTrades;
import scot.wildcamping.wildscotland.model.Trade;

public class SentTradesFragment extends Fragment {

	public SentTradesFragment(){}

    final String sent = "Sent";
    final String received = "Received";

    //SparseArray<Trade> activeTrades;
    SparseArray<Trade> sentTrades = new SparseArray<>();

    StoredTrades trades;

    private TradeListAdapter adapter;
    private ListView mDrawerList;


	@Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_open_trades, container, false);      //fragment_open_trades

        mDrawerList = (ListView) rootView.findViewById(R.id.open_trades_listview);
        TextView empty = (TextView) rootView.findViewById(R.id.empty);

        trades = new StoredTrades();
        //activeTrades = new SparseArray<>();
        sentTrades = trades.getActiveTrades();

        if(sentTrades.size() == 0){
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                if(sentTrades.get(position).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", sentTrades.get(position).getUnique_tid());
                intent.putExtra("send_cid", sentTrades.get(position).getSend_cid());
                intent.putExtra("recieve_cid", sentTrades.get(position).getRecieve_cid());
                intent.putExtra("date", sentTrades.get(position).getDate());
                startActivity(intent);
            }
        });

        adapter = new TradeListAdapter(getActivity(), sentTrades);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}