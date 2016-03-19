package scot.wildcamping.wildscotland.model;

import android.util.SparseArray;

/**
 * Created by Chris on 12-Mar-16.
 */
public class StoredTrades {

    private static int activeTradesSize;
    private static SparseArray<Trade> activeTrades = new SparseArray<>();

    private static int sentTradesSize;
    private static SparseArray<Trade> sentTrades = new SparseArray<>();

    private static int receivedTradesSize;
    private static SparseArray<Trade> receivedTrades = new SparseArray<>();

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

    public void setSentTrades(SparseArray<Trade> map) {
        this.sentTrades = map;
    }

    public SparseArray<Trade> getSentTrades(){
        return sentTrades;
    }

    public void setReceivedTrades(SparseArray<Trade> map){
        this.receivedTrades = map;
    }

    public SparseArray<Trade> getReceivedTrades(){
        return receivedTrades;
    }

    public void setSentTradesSize(int sent){
        this.sentTradesSize = sent;
    }

    public int getSentTradesSize(){
        return sentTradesSize;
    }

    public void setReceivedTradesSize(int received){
        this.receivedTradesSize = received;
    }

    public int getReceivedTradesSize(){
        return receivedTradesSize;
    }

}
