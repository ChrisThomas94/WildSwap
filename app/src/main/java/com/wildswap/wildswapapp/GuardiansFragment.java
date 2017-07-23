package com.wildswap.wildswapapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wildswap.wildswapapp.Adapters.UserListAdapter;
import com.wildswap.wildswapapp.Adapters.WrapContentHeightViewPager;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchUsers;
import com.wildswap.wildswapapp.AsyncTask.GiftSite;
import com.wildswap.wildswapapp.Objects.StoredData;
import com.wildswap.wildswapapp.Objects.User;

import java.util.ArrayList;

public class GuardiansFragment extends android.support.v4.app.Fragment {

    public GuardiansFragment() {

    }

    ListView mDrawerList;
    UserListAdapter adapter;
    SparseArray<User> fetchedUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_gift_site, container, false);

        mDrawerList = (ListView)rootView.findViewById(R.id.user_listview);

        StoredData inst = new StoredData();
        fetchedUsers = inst.getGuardians();

        adapter = new UserListAdapter(getActivity(), fetchedUsers);
        mDrawerList.setAdapter(adapter);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Intent profile = new Intent(getActivity(), ProfileActivity.class);
                profile.putExtra("email", fetchedUsers.get(position).getEmail());
                profile.putExtra("this_user", false);

                ArrayList<String> users = new ArrayList<>();
                users.add(0, fetchedUsers.get(position).getEmail());

                new FetchUsers(getActivity(), users, true, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        startActivity(profile);
                    }
                }).execute();

            }
        });

        return rootView;
    }

}

