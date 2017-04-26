package scot.wildcamping.wildscotland;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import scot.wildcamping.wildscotland.Adapters.BadgeListAdapter;
import scot.wildcamping.wildscotland.Objects.Badge;
import scot.wildcamping.wildscotland.Objects.Badges;

public class BadgesFragment extends Fragment {

    public BadgesFragment() {
    }

    BadgeListAdapter adapter;
    ListView mDrawerList;
    Badges inst = new Badges();
    SparseBooleanArray collection;
    SparseIntArray badges = new SparseIntArray();

    SparseArray<Badge> allBadges = new SparseArray<>();
    Badge thisBadge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_badges_list, container, false);

        for(int i = 0; i<25; i++){
            thisBadge = new Badge();
            String uri = "@drawable/badge_icon_" + (i+1);
            String title = "@string/badge_title_" + (i+1);
            String desc = "@string/badge_desc_" + (i+1);

            thisBadge.setResource(getResources().getIdentifier(uri, null, getActivity().getPackageName()));
            thisBadge.setTitle(getResources().getString(getResources().getIdentifier(title, null, getActivity().getPackageName())));
            thisBadge.setDescription(getResources().getString(getResources().getIdentifier(desc, null, getActivity().getPackageName())));
            allBadges.put(i, thisBadge);
            //badges.put(i, getResources().getIdentifier(uri, null, getActivity().getPackageName()));
        }

        inst.setBadges(allBadges);
        //inst.setBadgesResource(badges);

        System.out.println("title 1: " + inst.getBadges().get(1).getTitle());
        System.out.println("title 2: " + inst.getBadges().get(2).getTitle());
        System.out.println("title 3: " + inst.getBadges().get(3).getTitle());
        System.out.println("title 4: " + inst.getBadges().get(4).getTitle());



        mDrawerList = (ListView) rootView.findViewById(R.id.badge_listview);

        collection = inst.getCollection();

        adapter = new BadgeListAdapter(getActivity(), collection, badges, allBadges);

        mDrawerList.setAdapter(adapter);

        return rootView;
    }
}


