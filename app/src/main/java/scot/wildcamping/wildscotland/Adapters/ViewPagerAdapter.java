package scot.wildcamping.wildscotland.Adapters;

/**
 * Created by Chris on 01-Apr-16.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import scot.wildcamping.wildscotland.BadgesFragment;
import scot.wildcamping.wildscotland.BioFragment;
import scot.wildcamping.wildscotland.SentTradesFragment;
import scot.wildcamping.wildscotland.KnownSitesFragment;
import scot.wildcamping.wildscotland.ReceivedTradesFragment;
import scot.wildcamping.wildscotland.QuestionFragment;
import scot.wildcamping.wildscotland.YourSitesFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


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
            } else if (Titles[0] == "Bio"){
                tab1 = new BioFragment();
            } else {
                tab1 = new YourSitesFragment();
            }
            return tab1;
        }
        else if (position == 1)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Fragment tab2;
            if(Titles[1] == "Sent"){
                tab2 = new SentTradesFragment();
            } else if(Titles[1] == "Questions"){
                tab2 = new QuestionFragment();
            } else {
                tab2 = new KnownSitesFragment();
            }
            return tab2;
        } else if (position == 2){

            Fragment tab3;
            if(Titles[2] == "Badges"){
                tab3 = new BadgesFragment();
            } else {
                tab3 = null;
            }
            return tab3;

        } else {

        }
        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

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