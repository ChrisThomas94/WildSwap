package scot.wildcamping.wildscotland;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SiteSearchFragment extends Fragment {
	
	public SiteSearchFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_site_search, container, false);
         
        return rootView;
    }
}
