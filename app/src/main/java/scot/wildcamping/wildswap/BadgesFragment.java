package scot.wildcamping.wildswap;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import scot.wildcamping.wildswap.Adapters.BadgeListAdapter;
import scot.wildcamping.wildswap.Objects.Badge;
import scot.wildcamping.wildswap.Objects.Badges;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.User;

public class BadgesFragment extends Fragment {

    public BadgesFragment() {
    }

    BadgeListAdapter adapter;
    NonScrollListView mDrawerList;
    StoredData stored;
    User user;
    Badges inst = new Badges();
    SparseBooleanArray collection;
    ArrayList<Integer> badges = new ArrayList<>();
    Boolean thisUser;

    TextView tally;

    SparseArray<Badge> allBadges = new SparseArray<>();
    Badge thisBadge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_badges_list, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            thisUser = extras.getBoolean("this_user");
        }

        stored = new StoredData();

        if(thisUser) {
            user = stored.getLoggedInUser();
        } else {
            user = stored.getOtherUser();
        }

        tally = (TextView) rootView.findViewById(R.id.count);

        for(int i = 0; i<25; i++){
            thisBadge = new Badge();
            String uri = "@mipmap/badge_icon_" +(i+1);
            String title = "@string/badge_title_" + (i+1);
            String desc = "@string/badge_desc_" + (i+1);

            thisBadge.setResource(getResources().getIdentifier(uri, null, getActivity().getPackageName()));
            thisBadge.setTitle(getResources().getString(getResources().getIdentifier(title, null, getActivity().getPackageName())));
            thisBadge.setDescription(getResources().getString(getResources().getIdentifier(desc, null, getActivity().getPackageName())));
            allBadges.put(i, thisBadge);
        }

        inst.setBadges(allBadges);

        badges = user.getBadges();

        int count = 0;
        for(int i = 0; i<badges.size(); i++){
            if(badges.get(i) == 1){
                count++;
            }
        }

        tally.setText(String.valueOf(count) + "/" + String.valueOf(badges.size()));

        mDrawerList = (NonScrollListView) rootView.findViewById(R.id.badge_listview);

        collection = inst.getCollection();

        adapter = new BadgeListAdapter(getActivity(), collection, badges, allBadges);

        mDrawerList.setAdapter(adapter);

        return rootView;
    }
}


