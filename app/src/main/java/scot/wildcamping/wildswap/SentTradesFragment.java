package scot.wildcamping.wildswap;

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

import scot.wildcamping.wildswap.Adapters.TradeListAdapter;
import scot.wildcamping.wildswap.Objects.StoredTrades;
import scot.wildcamping.wildswap.Objects.Trade;

public class SentTradesFragment extends Fragment {

	public SentTradesFragment(){}

    final String sent = "Sent";
    final String received = "Received";

    //SparseArray<Trade> activeTrades;
    SparseArray<Trade> sentTrades = new SparseArray<>();
    SparseArray<Trade> openSentTrades;
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
        sentTrades = trades.getSentTrades();
        openSentTrades = new SparseArray<>();
        int j = 0;

        for(int i = 0; i<sentTrades.size(); i++){

            if(sentTrades.get(i).getStatus() == 0){
                openSentTrades.put(j, sentTrades.get(i));
                j++;
            }
        }

        if(openSentTrades.size() == 0){
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), TradeView.class);
                intent.putExtra("sent", true);
                intent.putExtra("unique_tid", openSentTrades.get(position).getUnique_tid());
                intent.putExtra("send_cid", openSentTrades.get(position).getSend_cid());
                intent.putExtra("recieve_cid", openSentTrades.get(position).getRecieve_cid());
                intent.putExtra("date", openSentTrades.get(position).getDate());
                startActivity(intent);
            }
        });

        adapter = new TradeListAdapter(getActivity(), openSentTrades);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }
}
