package com.wildswap.wildswapapp.Objects;

import java.util.ArrayList;

/**
 * Created by Chris on 10-Apr-16.
 *
 */
public class User {

    private String uid;
    private String name;
    private String email;
    private String bio;
    private String why;
    private Boolean verified;
    private String profile_pic;
    private String cover_pic;
    private String token;
    private String userType;
    private ArrayList<Integer> answers;
    private int numSites;
    private int numTrades;
    private int numVouch;
    private int numGifted;
    private int numReported;
    private ArrayList<Integer> badges;
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNumGifted() {
        return numGifted;
    }

    public int getNumReported() {
        return numReported;
    }

    public void setNumGifted(int numGifted) {
        this.numGifted = numGifted;
    }

    public void setNumReported(int numReported) {
        this.numReported = numReported;
    }

    public ArrayList<Integer> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<Integer> badges) {
        this.badges = badges;
    }

    public int getNumVouch() {
        return numVouch;
    }

    public void setNumVouch(int numVouch) {
        this.numVouch = numVouch;
    }

    public void setNumSites(int numSites) {
        this.numSites = numSites;
    }

    public void setNumTrades(int numTrades) {
        this.numTrades = numTrades;
    }

    public int getNumSites() {
        return numSites;
    }

    public int getNumTrades() {
        return numTrades;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public String getToken() {
        return token;
    }

    public void setAnswers(ArrayList<Integer> answers) {
        this.answers = answers;
    }

    public ArrayList<Integer> getAnswers() {
        return answers;
    }

    public String getProfile_pic() {
        return profile_pic;
    }


    public void setUid(String uid){
        this.uid = uid;
    }

    public String getUid(){
        return uid;
    }

    public void setWhy(String why){
        this.why = why;
    }

    public String getWhy(){
        return why;
    }

    public void setVerified(Boolean verified){
        this.verified = verified;
    }

    public Boolean getVerified(){
        return verified;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public String getBio(){
        return bio;
    }
}
