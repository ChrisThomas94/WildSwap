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

import scot.wildcamping.wildscotland.slidingmenu.adapter.SiteListAdapter;

public class YourSitesFragment extends Fragment {
	
	public YourSitesFragment(){}

    knownSite inst;
    SparseArray<Site> ownedSites;
    int ownedSize;
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
        ownedSize = inst.getOwnedSiteSize();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), OwnedSiteActivity.class);
                intent.putExtra("latitude", ownedSites.get(position).getPosition().latitude);
                intent.putExtra("longitude", ownedSites.get(position).getPosition().longitude);
                intent.putExtra("cid", ownedSites.get(position).getCid());
                intent.putExtra("title", ownedSites.get(position).getTitle());
                intent.putExtra("description", ownedSites.get(position).getDescription());
                intent.putExtra("rating", ownedSites.get(position).getRating());
                intent.putExtra("feature1", ownedSites.get(position).getFeature1());
                intent.putExtra("feature2", ownedSites.get(position).getFeature2());
                intent.putExtra("feature3", ownedSites.get(position).getFeature3());
                intent.putExtra("feature4", ownedSites.get(position).getFeature4());
                intent.putExtra("feature5", ownedSites.get(position).getFeature5());
                intent.putExtra("feature6", ownedSites.get(position).getFeature6());
                intent.putExtra("feature7", ownedSites.get(position).getFeature7());
                intent.putExtra("feature8", ownedSites.get(position).getFeature8());
                intent.putExtra("feature9", ownedSites.get(position).getFeature9());
                intent.putExtra("feature10", ownedSites.get(position).getFeature10());
                startActivity(intent);            }
        });

        adapter = new SiteListAdapter(getActivity(), ownedSites);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }

}
