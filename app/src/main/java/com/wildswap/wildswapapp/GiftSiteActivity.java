package com.wildswap.wildswapapp;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wildswap.wildswapapp.Adapters.UserListAdapter;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.GiftSite;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

public class GiftSiteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public GiftSiteActivity() {
    }

    ListView mDrawerList;
    UserListAdapter adapter;
    SparseArray<User> fetchedUsers;
    String cid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_site);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cid = extras.getString("cid");
        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerList = (ListView)findViewById(R.id.user_listview);

        StoredData inst = new StoredData();
        fetchedUsers = inst.getDealers();

        adapter = new UserListAdapter(this, fetchedUsers);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(GiftSiteActivity.this);
                builder1.setTitle("Attention!");
                builder1.setMessage("You are about to reveal this location to another user, this is your last chance to change your mind.");

                builder1.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        new GiftSite(GiftSiteActivity.this, fetchedUsers.get(position).getUid(), cid, new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {

                                BadgeManager bM = new BadgeManager(GiftSiteActivity.this);
                                bM.checkGiftedBadges();

                                Toast.makeText(GiftSiteActivity.this, output, Toast.LENGTH_LONG).show();

                            }
                        }).execute();

                    }
                });

                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert1 = builder1.create();
                alert1.show();

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_search, menu);

        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("new text " + newText);
        adapter.getFilter().filter(newText);

        return true;
    }
}

