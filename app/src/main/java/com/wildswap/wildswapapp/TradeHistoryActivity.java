package com.wildswap.wildswapapp;

/**
 * Created by Chris on 01-Apr-16.
 *
 */
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wildswap.wildswapapp.Adapters.TradeListAdapter;
import com.wildswap.wildswapapp.Objects.StoredTrades;
import com.wildswap.wildswapapp.Objects.Trade;

public class TradeHistoryActivity extends AppCompatActivity {

    public TradeHistoryActivity(){}

    final String sent = "Sent";
    final String received = "Received";

    SparseArray<Trade> inactiveTrades;
    StoredTrades trades;

    private TradeListAdapter adapter;
    private ListView mDrawerList;
    View parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_history);

        parentLayout = findViewById(android.R.id.content);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        //View rootView = inflater.inflate(R.layout.activity_trade_history, container, false);      //fragment_open_trades

        mDrawerList = (ListView)findViewById(R.id.closed_trades_listview);

        TextView empty = (TextView)findViewById(R.id.empty);
        
        trades = new StoredTrades();
        inactiveTrades = new SparseArray<>();
        inactiveTrades = trades.getInactiveTrades();

        System.out.println("inactive trades: "+inactiveTrades.size());

        if(inactiveTrades.size() == 0){
            empty.setVisibility(View.VISIBLE);
            mDrawerList.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            mDrawerList.setVisibility(View.VISIBLE);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(isNetworkAvailable()) {

                    Intent intent = new Intent(getBaseContext(), TradeView.class);

                    if (inactiveTrades.get(position).getUserRelation().equals(sent)) {
                        intent.putExtra("sent", true);
                    } else {
                        intent.putExtra("received", true);
                    }

                    intent.putExtra("status", inactiveTrades.get(position).getStatus());
                    intent.putExtra("unique_tid", inactiveTrades.get(position).getUnique_tid());
                    intent.putExtra("send_cid", inactiveTrades.get(position).getSend_cid());
                    intent.putExtra("recieve_cid", inactiveTrades.get(position).getRecieve_cid());
                    intent.putExtra("date", inactiveTrades.get(position).getDate());
                    startActivity(intent);
                } else {
                    Snackbar.make(parentLayout, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        adapter = new TradeListAdapter(getBaseContext(), inactiveTrades);
        mDrawerList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
