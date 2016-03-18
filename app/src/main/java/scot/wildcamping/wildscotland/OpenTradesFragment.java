package scot.wildcamping.wildscotland;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import scot.wildcamping.wildscotland.slidingmenu.adapter.SiteListAdapter;
import scot.wildcamping.wildscotland.slidingmenu.adapter.TradeListAdapter;

public class OpenTradesFragment extends Fragment{
	
	public OpenTradesFragment(){}

    final String sent = "Sent";
    final String received = "Received";

    SparseArray<Trade> activeTrades;
    StoredTrades trades;

    private TradeListAdapter adapter;
    private ListView mDrawerList;


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_open_trades, container, false);      //fragment_open_trades

        mDrawerList = (ListView) rootView.findViewById(R.id.open_trades_listview);

        trades = new StoredTrades();
        activeTrades = new SparseArray<>();
        activeTrades = trades.getActiveTrades();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                if(activeTrades.get(position).getUserRelation().equals(sent)){
                    intent = new Intent(getActivity(), TradeView_Sent.class);
                } else {
                    intent = new Intent(getActivity(), TradeView_Received.class);
                }
                intent.putExtra("unique_tid", activeTrades.get(position).getUnique_tid());
                intent.putExtra("send_cid", activeTrades.get(position).getSend_cid());
                intent.putExtra("recieve_cid", activeTrades.get(position).getRecieve_cid());
                startActivity(intent);
            }
        });

        adapter = new TradeListAdapter(getActivity(), activeTrades);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }

}
