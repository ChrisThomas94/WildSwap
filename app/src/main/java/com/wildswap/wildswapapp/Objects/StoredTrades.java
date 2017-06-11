package com.wildswap.wildswapapp.Objects;

import android.util.SparseArray;

/**
 * Created by Chris on 12-Mar-16.
 */
public class StoredTrades {

    private static int allTradesSize;
    private static SparseArray<Trade> allTrades = new SparseArray<>();

    private static int activeTradesSize;
    private static SparseArray<Trade> activeTrades = new SparseArray<>();

    private static int inactiveTradesSize;
    private static SparseArray<Trade> inactiveTrades = new SparseArray<>();

    private static int sentTradesSize;
    private static SparseArray<Trade> sentTrades = new SparseArray<>();

    private static int receivedTradesSize;
    private static SparseArray<Trade> receivedTrades = new SparseArray<>();

    private static int acceptedTradesSize;
    private static SparseArray<Trade> acceptedTrades = new SparseArray<>();

    private static int rejectedTradesSize;
    private static SparseArray<Trade> rejectedTrades = new SparseArray<>();

    public int getAcceptedTradesSize() {
        return acceptedTradesSize;
    }

    public SparseArray<Trade> getAcceptedTrades() {
        return acceptedTrades;
    }

    public int getRejectedTradesSize() {
        return rejectedTradesSize;
    }

    public SparseArray<Trade> getRejectedTrades() {
        return rejectedTrades;
    }

    public void setAcceptedTradesSize(int acceptedTradesSize) {
        StoredTrades.acceptedTradesSize = acceptedTradesSize;
    }

    public void setAcceptedTrades(SparseArray<Trade> acceptedTrades) {
        StoredTrades.acceptedTrades = acceptedTrades;
    }

    public void setRejectedTradesSize(int rejectedTradesSize) {
        StoredTrades.rejectedTradesSize = rejectedTradesSize;
    }

    public void setRejectedTrades(SparseArray<Trade> rejectedTrades) {
        StoredTrades.rejectedTrades = rejectedTrades;
    }

    public void setAllTradesSize(int allTradesSize) {
        this.allTradesSize = allTradesSize;
    }

    public int getAllTradesSize(){
        return allTradesSize;
    }

    public void setAllTrades(SparseArray<Trade> map){
        this.allTrades = map;
    }

    public SparseArray<Trade> getAllTrades(){
        return allTrades;
    }

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

    public void setInactiveTradesSize(int inactiveTradesSize) {
        this.inactiveTradesSize = inactiveTradesSize;
    }

    public int getInactiveTradesSize(){
        return inactiveTradesSize;
    }

    public void setInactiveTrades(SparseArray<Trade> map){
        this.inactiveTrades = map;
    }

    public SparseArray<Trade> getInactiveTrades(){
        return inactiveTrades;
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

    public void clear(){

        allTrades.clear();
        inactiveTrades.clear();
        activeTrades.clear();
        sentTrades.clear();
        receivedTrades.clear();
        acceptedTrades.clear();
        rejectedTrades.clear();
    }

}
