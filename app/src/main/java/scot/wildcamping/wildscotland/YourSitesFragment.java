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

import scot.wildcamping.wildscotland.adapter.SiteListAdapter;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class YourSitesFragment extends Fragment {
	
	public YourSitesFragment(){}

    knownSite inst;
    SparseArray<Site> ownedSites;
    private SiteListAdapter adapter;
    private ListView mDrawerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_your_sites, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.known_sites_listview);

        inst = new knownSite();
        ownedSites = new SparseArray<>();
        ownedSites = inst.getOwnedSitesMap();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), OwnedSiteActivity.class);
                intent.putExtra("arrayPosition", position);
                //intent.putExtra("latitude", ownedSites.get(position).getPosition().latitude);
                //intent.putExtra("longitude", ownedSites.get(position).getPosition().longitude);
                intent.putExtra("cid", ownedSites.get(position).getCid());
                //intent.putExtra("title", ownedSites.get(position).getTitle());
                //intent.putExtra("description", ownedSites.get(position).getDescription());
                //intent.putExtra("rating", ownedSites.get(position).getRating());
                //intent.putExtra("image", ownedSites.get(position).getImage());
                intent.putExtra("prevState", 1);
                startActivity(intent);
            }
        });

        adapter = new SiteListAdapter(getActivity(), ownedSites);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }

}
