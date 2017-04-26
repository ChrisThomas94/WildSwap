package scot.wildcamping.wildscotland.Objects;

import android.util.SparseArray;

/**
 * Created by Chris on 04-Mar-16.
 *
 */
public class StoredData {

    private static int knownSiteSize;
    private static int unknownSitesSize;
    private static int ownedSitesSize;
    private static int tempSize;

    private static SparseArray<Gallery> images = new SparseArray<>();
    private static SparseArray<Site> knownSitesMap = new SparseArray<>();
    private static SparseArray<Site> unknownSitesMap = new SparseArray<>();
    private static SparseArray<Site> ownedSitesMap = new SparseArray<>();
    private static SparseArray<Gallery> temp = new SparseArray<>();
    private static SparseArray<User> dealers = new SparseArray<>();
    private static User loggedInUser = new User();
    private static SparseArray<Badge> badgeCollection = new SparseArray<>();

    public static void setBadgeCollection(SparseArray<Badge> badgeCollection) {
        StoredData.badgeCollection = badgeCollection;
    }

    public static SparseArray<Badge> getBadgeCollection() {
        return badgeCollection;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setDealers(SparseArray<User> dealers) {
        this.dealers = dealers;
    }

    public SparseArray<User> getDealers() {
        return dealers;
    }

    public void setImages(SparseArray<Gallery> images){
        this.images = images;
    }

    public SparseArray<Gallery> getImages(){
        return images;
    }


    public void setKnownSiteSize(int siteSize) {
        this.knownSiteSize = siteSize;
    }

    public int getKnownSiteSize(){
        return knownSiteSize;
    }

    public void setUnknownSitesSize(int unknownSitesSize) {
        this.unknownSitesSize = unknownSitesSize;
    }

    public int getUnknownSitesSize(){
        return unknownSitesSize;
    }

    public void setKnownSitesMap(SparseArray<Site> map){
        this.knownSitesMap = map;
    }

    public void setOwnedSiteSize(int siteSize) {
        this.ownedSitesSize = siteSize;
    }

    public int getOwnedSiteSize(){
        return ownedSitesSize;
    }

    public SparseArray<Site> getKnownSitesMap(){
        return knownSitesMap;
    }

    public void setUnknownSitesMap(SparseArray<Site> map){
        this.unknownSitesMap = map;
    }

    public SparseArray<Site> getUnknownSitesMap(){
        return unknownSitesMap;
    }

    public void setOwnedSitesMap(SparseArray<Site> map){
        this.ownedSitesMap = map;
    }

    public SparseArray<Site> getOwnedSitesMap(){
        return ownedSitesMap;
    }

    public void setTemp(SparseArray<Gallery> temp){
        this.temp = temp;
    }

    public SparseArray<Gallery> getTemp(){
        return temp;
    }

}

