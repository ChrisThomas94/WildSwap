package scot.wildcamping.wildscotland;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class KnownSitesFragment extends Fragment {
	
	public KnownSitesFragment(){}

    RelativeLayout site1;
    RelativeLayout site2;
    RelativeLayout site3;
    RelativeLayout site4;
    RelativeLayout site5;
    RelativeLayout site6;
    RelativeLayout site7;
    RelativeLayout site8;
    RelativeLayout site9;
    RelativeLayout site10;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_known_sites, container, false);
         
        return rootView;
    }
}
