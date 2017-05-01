package scot.wildcamping.wildscotland;

import scot.wildcamping.wildscotland.Objects.StoredData;
import scot.wildcamping.wildscotland.Objects.StoredTrades;

/**
 * Created by Chris on 01-May-17.
 *
 */

public class BadgeManager {

    StoredData inst = new StoredData();
    StoredTrades trading = new StoredTrades();

    public void checkSiteBadges(){
        int ownedSize = inst.getOwnedSiteSize();

        if(ownedSize == 1){
            //unlock badge_2
        } else if (ownedSize == 2){
            //unlock badge_3
        } else if (ownedSize == 5){
            //unlock badge_4
        } else if (ownedSize == 10){
            //unlock badge_5
        }
    }

    public void checkTradeBadges(){
        int trades = trading.getAcceptedTradesSize();

        if(trades == 1){
            //unlock badge_6
        } else if (trades == 5){
            //unlock badge_7
        } else if (trades == 10){
            //unlock badge_8
        } else if (trades == 20){
            //unlock badge_9
        }
    }

    public void checkGiftedBadges(){


    }

}
