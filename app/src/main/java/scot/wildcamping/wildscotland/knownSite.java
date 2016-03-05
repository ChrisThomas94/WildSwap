package scot.wildcamping.wildscotland;

import android.support.v4.app.TaskStackBuilder;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 04-Mar-16.
 */
public class knownSite {

    private static int siteSize;
    private static int unknownSitesSize;

    private static SparseArray<Site> knownSitesMap = new SparseArray<>();
    private static SparseArray<Site> unknownSitesMap = new SparseArray<>();

    public void setSiteSize(int siteSize) {
        this.siteSize = siteSize;
    }

    public int getSiteSize(){
        return siteSize;
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

    public SparseArray<Site> getKnownSitesMap(){
        return knownSitesMap;
    }

    public void setUnknownSitesMap(SparseArray<Site> map){
        this.unknownSitesMap = map;
    }

    public SparseArray<Site> getUnknownSitesMap(){
        return unknownSitesMap;
    }

}

