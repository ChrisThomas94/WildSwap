package scot.wildcamping.wildscotland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import scot.wildcamping.wildscotland.Adapters.TradeListAdapter;
import scot.wildcamping.wildscotland.Objects.StoredTrades;
import scot.wildcamping.wildscotland.Objects.Trade;

public class ReceivedTradesFragment extends Fragment {
	
	public ReceivedTradesFragment(){}

    SparseArray<Trade> receivedTrades = new SparseArray<>();

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
        receivedTrades = trades.getReceivedTrades();

        for(int i = 0; i<receivedTrades.size(); i++){

            if(receivedTrades.get(i).getStatus() != 0){
                receivedTrades.remove(i);
            }

            System.out.println("trade left here:" +i);

        }

        if(receivedTrades.size() > 0){
            empty.setVisibility(View.GONE);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), TradeView.class);
                intent.putExtra("received", true);
                intent.putExtra("unique_tid", receivedTrades.get(position).getUnique_tid());
                intent.putExtra("send_cid", receivedTrades.get(position).getSend_cid());
                intent.putExtra("recieve_cid", receivedTrades.get(position).getRecieve_cid());
                intent.putExtra("date", receivedTrades.get(position).getDate());
                startActivity(intent);
            }
        });

        adapter = new TradeListAdapter(getActivity(), receivedTrades);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }
}
