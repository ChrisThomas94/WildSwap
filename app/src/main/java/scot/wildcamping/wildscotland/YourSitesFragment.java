package scot.wildcamping.wildscotland;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.w3c.dom.Text;

import scot.wildcamping.wildscotland.adapter.SiteListAdapter;
import scot.wildcamping.wildscotland.model.Site;
import scot.wildcamping.wildscotland.model.knownSite;

public class YourSitesFragment extends Fragment {
	
	public YourSitesFragment(){}

    knownSite inst;
    SparseArray<Site> ownedSites;
    private SiteListAdapter adapter;
    private ListView mDrawerList;
    boolean register = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_your_sites, container, false);

        mDrawerList = (ListView) rootView.findViewById(R.id.owned_sites_listview);

        TextView empty = (TextView) rootView.findViewById(R.id.empty);

        inst = new knownSite();
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

                Intent intent = new Intent(getActivity(), OwnedSiteActivity.class);
                intent.putExtra("arrayPosition", position);
                intent.putExtra("cid", ownedSites.get(position).getCid());
                intent.putExtra("prevState", 1);
                startActivity(intent);
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
