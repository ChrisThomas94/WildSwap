package scot.wildcamping.wildswap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import scot.wildcamping.wildswap.AsyncTask.AsyncResponse;
import scot.wildcamping.wildswap.AsyncTask.UpdateBadges;
import scot.wildcamping.wildswap.Objects.Site;
import scot.wildcamping.wildswap.Objects.StoredData;
import scot.wildcamping.wildswap.Objects.StoredTrades;
import scot.wildcamping.wildswap.Objects.User;

/**
 * Created by Chris on 01-May-17.
 *
 */

public class BadgeManager {

    private Context context;
    StoredData inst = new StoredData();
    StoredTrades allTrades = new StoredTrades();
    private StoredTrades trading = new StoredTrades();
    private User thisUser = inst.getLoggedInUser();
    private ArrayList<Integer> badges = thisUser.getBadges();
    Boolean update = false;
    private int newBadge = 0;

    public BadgeManager(Context context){
        this.context = context;

        System.out.println("i am in badge manager");
    }

    public void awardJoinBadge(){
        ArrayList<Integer> existingBadges = new ArrayList<>(badges);

        badges.set(0, 1);
        newBadge = 1;

        if(!badges.equals(existingBadges)){
            updateBadges();
        }

        thisUser.setBadges(badges);

    }


    public void checkSiteBadges(){
        int ownedSize = inst.getOwnedSiteSize();
        ArrayList<Integer> existingBadges = new ArrayList<>(badges);

        if(ownedSize >= 1 && ownedSize < 2){
            //unlock badge_2
            badges.set(1,1);
            newBadge = 2;
            update = true;
        } else if (ownedSize >= 2 && ownedSize < 5){
            //unlock badge_3
            badges.set(1,1);
            badges.set(2,1);
            newBadge = 3;
            update = true;

        } else if (ownedSize >= 5 && ownedSize < 10){
            //unlock badge_4
            badges.set(1,1);
            badges.set(2,1);
            badges.set(3,1);
            newBadge = 4;
            update = true;

        } else if (ownedSize >= 10){
            //unlock badge_5
            badges.set(1,1);
            badges.set(2,1);
            badges.set(3,1);
            badges.set(4,1);
            newBadge = 5;
            update = true;
        } else {
            badges.set(1,0);
            badges.set(2,0);
            badges.set(3,0);
            badges.set(4,0);
            update = true;
        }

        //is this comparison robust??
        if(!badges.equals(existingBadges)){
            updateBadges();
        }

        thisUser.setBadges(badges);
    }

    public void checkTradeBadges(){
        //int trades = trading.getAcceptedTradesSize();
        ArrayList<Integer> existingBadges = new ArrayList<>(badges);

        int trades = 0;

        for(int i = 0; i<allTrades.getInactiveTradesSize(); i++){
            if(allTrades.getInactiveTrades().get(i).getStatus() == 2){
                trades++;
            }
        }

        if(trades >= 1 && trades <5){
            //unlock badge_6
            badges.set(5,1);
            newBadge = 6;
            update = true;

        } else if (trades >= 5 && trades <10 ){
            //unlock badge_7
            badges.set(5,1);
            badges.set(6,1);
            newBadge = 7;
            update = true;

        } else if (trades >=10 && trades < 20){
            //unlock badge_8
            badges.set(5,1);
            badges.set(6,1);
            badges.set(7,1);
            newBadge = 8;
            update = true;

        } else if (trades >= 20){
            //unlock badge_9
            badges.set(5,1);
            badges.set(6,1);
            badges.set(7,1);
            badges.set(8,1);
            newBadge = 9;
            update = true;
        } else {
            badges.set(5,0);
            badges.set(6,0);
            badges.set(7,0);
            badges.set(8,0);
            update = true;
        }

        thisUser.setBadges(badges);
        if(update && !badges.equals(existingBadges)){
            updateBadges();
        }
    }

    public void checkGiftedBadges(){
        int gifted = thisUser.getNumGifted();
        ArrayList<Integer> existingBadges = new ArrayList<>(badges);

        if(gifted >= 1 && gifted < 5){
            // unlock badge 10
            badges.set(9, 1);
            newBadge = 10;
            update = true;

        } else if(gifted >= 5 && gifted < 10){
            // unlock badge 11
            badges.set(9,1);
            badges.set(10,1);
            newBadge = 11;
            update = true;

        } else if(gifted >= 10 && gifted < 20){
            // unlock badge 12
            badges.set(9,1);
            badges.set(10,1);
            badges.set(11,1);
            newBadge = 12;
            update = true;

        } else if(gifted >= 20){
            // unlock badge 13
            badges.set(9,1);
            badges.set(10,1);
            badges.set(11,1);
            badges.set(12,1);
            newBadge = 13;
            update = true;
        } else {
            badges.set(9,0);
            badges.set(10,0);
            badges.set(11,0);
            badges.set(12,0);
            update = true;
        }

        thisUser.setBadges(badges);
        if(update && !badges.equals(existingBadges)){
            updateBadges();
        }
    }

    public void checkReportedBadges(){
        int reports = thisUser.getNumReported();
        ArrayList<Integer> existingBadges = new ArrayList<>(badges);

        if(reports >= 1 && reports < 5){
            //unlock badge 14
            badges.set(13,1);
            newBadge = 14;
            update = true;

        } else if(reports >= 5 && reports <10){
            //unlock badge 15
            badges.set(13,1);
            badges.set(14,1);
            newBadge = 15;
            update = true;

        } else if(reports >= 10 && reports < 20){
            //unlock badge 16
            badges.set(13,1);
            badges.set(14,1);
            badges.set(15,1);
            newBadge = 16;
            update = true;

        } else if(reports >= 20){
            //unlock badge 17
            badges.set(13,1);
            badges.set(14,1);
            badges.set(15,1);
            badges.set(16,1);
            newBadge = 17;
            update = true;
        } else {
            badges.set(13,0);
            badges.set(14,0);
            badges.set(15,0);
            badges.set(16,0);
            update = true;
        }

        thisUser.setBadges(badges);
        if(update && !badges.equals(existingBadges)){
            updateBadges();
        }
    }

    public void checkContributorBadges(){

        thisUser.setBadges(badges);

    }

    public void checkCountryBadges(){
        SparseArray<Site> ownedSites = inst.getOwnedSitesMap();
        List<String> countries = new ArrayList<>();
        ArrayList<Integer> existingBadges = new ArrayList<>(badges);


        for(int i= 0; i<ownedSites.size(); i++){
            countries.add(i, ownedSites.get(i).getAddress().get(0).getCountryName());
        }

        Set<String> uniqueCountries = new HashSet<>(countries);

        int unique = uniqueCountries.size();

        if(unique > 1 && unique <= 2){
            //unlock badge 22
            badges.set(21,1);
            newBadge = 22;
            update = true;

        } else if(unique >= 5 && unique < 10){
            //unlock badge 23
            badges.set(21,1);
            badges.set(22,1);
            newBadge = 23;
            update = true;

        } else if (unique >= 10 && unique < 15){
            //unlock badge 24
            badges.set(21,1);
            badges.set(22,1);
            badges.set(23,1);
            newBadge = 24;
            update = true;

        } else if(unique >= 15){
            //unlock badge 25
            badges.set(21,1);
            badges.set(22,1);
            badges.set(23,1);
            badges.set(24,1);
            newBadge = 25;
            update = true;
        } else {
            badges.set(21,0);
            badges.set(22,0);
            badges.set(23,0);
            badges.set(24,0);
            update = true;
        }

        thisUser.setBadges(badges);
        if(update && !badges.equals(existingBadges)){
            updateBadges();
        }
    }

    private void updateBadges(){

        System.out.println("i tried to update badges");

        if(newBadge != 0) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("New Badge Unlocked!");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.badge_popup, null);

            final TextView badgeTitle = (TextView) dialogView.findViewById(R.id.badgeTitle);
            final ImageView badgeThumbnail = (ImageView) dialogView.findViewById(R.id.badgeThumbnail);
            final TextView badgeDesc = (TextView) dialogView.findViewById(R.id.badgeDesc);

            final String uri = "@mipmap/badge_icon_" + (newBadge);
            final String title = "@string/badge_title_" + (newBadge);
            final String desc = "@string/badge_desc_" + (newBadge);

            badgeTitle.setText(context.getResources().getIdentifier(title, null, context.getPackageName()));
            badgeThumbnail.setImageResource(context.getResources().getIdentifier(uri, null, context.getPackageName()));
            badgeDesc.setText(context.getResources().getIdentifier(desc, null, context.getPackageName()));

            builder1.setView(dialogView);

            builder1.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            final AlertDialog alert1 = builder1.create();

            alert1.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alert1.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

                }
            });

            alert1.show();

            new UpdateBadges(context, true, new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    alert1.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);

                }
            }).execute();
        }

    }
}
