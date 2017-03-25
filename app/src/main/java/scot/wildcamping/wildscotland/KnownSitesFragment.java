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

import scot.wildcamping.wildscotland.Adapters.SiteListAdapter;
import scot.wildcamping.wildscotland.Objects.Site;
import scot.wildcamping.wildscotland.Objects.knownSite;

public class KnownSitesFragment extends Fragment {
	
	public KnownSitesFragment(){}

    knownSite inst;
    SparseArray<Site> knownSites;
    private SiteListAdapter adapter;
    private ListView mDrawerList;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_known_sites, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.known_sites_listview);

        TextView empty = (TextView) rootView.findViewById(R.id.empty);

        inst = new knownSite();
        knownSites = new SparseArray<>();
        knownSites = inst.getKnownSitesMap();

        if(knownSites.size() == 0){
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), KnownSiteActivity.class);
                intent.putExtra("arrayPosition", position);
                //intent.putExtra("latitude", knownSites.get(position).getPosition().latitude);
                //intent.putExtra("longitude", knownSites.get(position).getPosition().longitude);
                intent.putExtra("cid", knownSites.get(position).getCid());
                //intent.putExtra("title", knownSites.get(position).getTitle());
                //intent.putExtra("description", knownSites.get(position).getDescription());
                //intent.putExtra("rating", knownSites.get(position).getRating());
                //intent.putExtra("profilePicString", knownSites.get(position).getImage());
                intent.putExtra("prevState", 2);
                startActivity(intent);
            }
        });

        adapter = new SiteListAdapter(getActivity(), knownSites);
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
