package scot.wildcamping.wildscotland;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OpenTradesFragment extends Fragment {
	
	public OpenTradesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_open_trades, container, false);      //fragment_open_trades
         
        return rootView;
    }
}
