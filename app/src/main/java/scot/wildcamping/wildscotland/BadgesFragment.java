package scot.wildcamping.wildscotland;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import scot.wildcamping.wildscotland.Adapters.BadgeListAdapter;
import scot.wildcamping.wildscotland.Objects.Badges;

public class BadgesFragment extends Fragment {

    public BadgesFragment() {
    }

    BadgeListAdapter adapter;
    ListView mDrawerList;
    Badges inst = new Badges();
    SparseBooleanArray collection;
    SparseIntArray badges = new SparseIntArray();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_badges_list, container, false);

        for(int i = 0; i<=25; i++){
            String uri = "@drawable/badge_icon_" + i;
            badges.put(i, getResources().getIdentifier(uri, null, getActivity().getPackageName()));
        }


        inst.setBadgesResource(badges);

        mDrawerList = (ListView) rootView.findViewById(R.id.badge_listview);

        collection = inst.getCollection();

        adapter = new BadgeListAdapter(getActivity(), collection, badges);
        mDrawerList.setAdapter(adapter);

        return rootView;
    }
}


