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

public class KnownSitesFragment extends Fragment{
	
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

        inst = new knownSite();
        knownSites = new SparseArray<>();
        knownSites = inst.getKnownSitesMap();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), KnownSiteActivity.class);
                intent.putExtra("latitude", knownSites.get(position).getPosition().latitude);
                intent.putExtra("longitude", knownSites.get(position).getPosition().longitude);
                intent.putExtra("cid", knownSites.get(position).getCid());
                intent.putExtra("title", knownSites.get(position).getTitle());
                intent.putExtra("description", knownSites.get(position).getDescription());
                intent.putExtra("rating", knownSites.get(position).getRating());
                intent.putExtra("feature1", knownSites.get(position).getFeature1());
                intent.putExtra("feature2", knownSites.get(position).getFeature2());
                intent.putExtra("feature3", knownSites.get(position).getFeature3());
                intent.putExtra("feature4", knownSites.get(position).getFeature4());
                intent.putExtra("feature5", knownSites.get(position).getFeature5());
                intent.putExtra("feature6", knownSites.get(position).getFeature6());
                intent.putExtra("feature7", knownSites.get(position).getFeature7());
                intent.putExtra("feature8", knownSites.get(position).getFeature8());
                intent.putExtra("feature9", knownSites.get(position).getFeature9());
                intent.putExtra("feature10", knownSites.get(position).getFeature10());
                startActivity(intent);
            }
        });

        adapter = new SiteListAdapter(getActivity(), knownSites);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }

}
