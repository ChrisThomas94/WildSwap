package com.wildswap.wildswapapp;

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

import com.wildswap.wildswapapp.Adapters.SiteListAdapter;
import com.wildswap.wildswapapp.AsyncTask.AsyncResponse;
import com.wildswap.wildswapapp.AsyncTask.FetchSiteImages;
import com.wildswap.wildswapapp.Objects.Gallery;
import com.wildswap.wildswapapp.Objects.Site;
import com.wildswap.wildswapapp.Objects.StoredData;


public class OwnedSitesFragment extends Fragment {
	
	public OwnedSitesFragment(){}

    StoredData inst;
    SparseArray<Site> ownedSites;
    SiteListAdapter adapter;
    ListView mDrawerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_your_sites, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.owned_sites_listview);

        TextView empty = (TextView) rootView.findViewById(R.id.empty);

        inst = new StoredData();
        ownedSites = new SparseArray<>();
        ownedSites = inst.getOwnedSitesMap();

        if(ownedSites.size() == 0){
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Intent intent = new Intent(getActivity(), SiteViewerActivity.class);
                intent.putExtra("arrayPosition", position);
                intent.putExtra("cid", ownedSites.get(position).getCid());
                intent.putExtra("prevState", 1);
                intent.putExtra("owned", true);

                SparseArray<Gallery> images = inst.getImages();
                String cid = ownedSites.get(position).getCid();
                String subCid = cid.substring(cid.length()-8);
                int cidEnd = Integer.parseInt(subCid);

                images.get(cidEnd, null);

                if(images.get(cidEnd) == null && isNetworkAvailable()){
                    new FetchSiteImages(getActivity(), ownedSites.get(position).getCid(), new AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            startActivity(intent);
                        }
                    }).execute();

                } else {
                    //no images previously fetched for this site
                    startActivity(intent);
                }

            }
        });

        adapter = new SiteListAdapter(getActivity(), ownedSites);
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
