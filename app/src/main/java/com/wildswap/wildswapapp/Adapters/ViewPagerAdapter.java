package com.wildswap.wildswapapp.Adapters;

/*
 *
 * Created by Chris on 01-Apr-16.
 *
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wildswap.wildswapapp.BadgesFragment;
import com.wildswap.wildswapapp.SentTradesFragment;
import com.wildswap.wildswapapp.KnownSitesFragment;
import com.wildswap.wildswapapp.ReceivedTradesFragment;
import com.wildswap.wildswapapp.QuestionFragment;
import com.wildswap.wildswapapp.OwnedSitesFragment;




public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Fragment tab1;
            if(Titles[0] == "Received"){
                tab1 = new ReceivedTradesFragment();
            } else if (Titles[0] == "Questions"){
                tab1 = new QuestionFragment();

            } else {
                tab1 = new OwnedSitesFragment();
            }
            return tab1;

            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        } else if (position == 1) {
            Fragment tab2;
            if(Titles[1] == "Sent"){
                tab2 = new SentTradesFragment();
            } else if(Titles[1] == "Badges"){
                tab2 = new BadgesFragment();

            } else {
                tab2 = new KnownSitesFragment();
            }
            return tab2;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}