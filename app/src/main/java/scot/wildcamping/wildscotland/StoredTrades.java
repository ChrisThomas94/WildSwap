package scot.wildcamping.wildscotland;

import android.util.SparseArray;

/**
 * Created by Chris on 12-Mar-16.
 */
public class StoredTrades {

    private static int activeTradesSize;
    private static SparseArray<Trade> activeTrades = new SparseArray<>();

    public void setActiveTradesSize(int activeTradesSize) {
        this.activeTradesSize = activeTradesSize;
    }

    public int getActiveTradesSize(){
        return activeTradesSize;
    }

    public void setActiveTrades(SparseArray<Trade> map){
        this.activeTrades = map;
    }

    public SparseArray<Trade> getActiveTrades(){
        return activeTrades;
    }

}
