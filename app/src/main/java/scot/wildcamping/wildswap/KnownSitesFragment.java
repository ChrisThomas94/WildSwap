package scot.wildcamping.wildswap;

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

import scot.wildcamping.wildswap.Adapters.SiteListAdapter;
import scot.wildcamping.wildswap.AsyncTask.AsyncResponse;
import scot.wildcamping.wildswap.AsyncTask.FetchSiteImages;
import scot.wildcamping.wildswap.Objects.Gallery;
import scot.wildcamping.wildswap.Objects.Site;
import scot.wildcamping.wildswap.Objects.StoredData;

public class KnownSitesFragment extends Fragment {
	
	public KnownSitesFragment(){}

    StoredData inst;
    SparseArray<Site> knownSites;
    private SiteListAdapter adapter;
    private ListView mDrawerList;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_known_sites, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.known_sites_listview);

        TextView empty = (TextView) rootView.findViewById(R.id.empty);

        inst = new StoredData();
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

                final Intent intent = new Intent(getActivity(), KnownSiteViewerActivity.class);
                intent.putExtra("arrayPosition", position);
                intent.putExtra("cid", knownSites.get(position).getCid());
                intent.putExtra("prevState", 2);

                SparseArray<Gallery> images = inst.getImages();
                String cid = knownSites.get(position).getCid();
                String subCid = cid.substring(cid.length()-8);
                int cidEnd = Integer.parseInt(subCid);

                images.get(cidEnd, null);

                if(images.get(cidEnd) == null && isNetworkAvailable()){
                    new FetchSiteImages(getActivity(), knownSites.get(position).getCid(), new AsyncResponse() {
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
