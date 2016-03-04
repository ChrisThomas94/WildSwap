package scot.wildcamping.wildscotland;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chris on 04-Mar-16.
 */
public class knownSite {

    private static List<LatLng> knownSites = new ArrayList<>();
    private static int siteSize;

    private static List<LatLng> unknownSites = new ArrayList<>();
    private static int unknownSitesSize;

    private LatLng position;
    private String title;
    private String description;
    private Double rating;
    private boolean feature1;
    private boolean feature2;
    private boolean feature3;
    private boolean feature4;
    private boolean feature5;
    private boolean feature6;
    private boolean feature7;
    private boolean feature8;
    private boolean feature9;
    private boolean feature10;

    public void setPosition(LatLng position){
        this.position = position;
    }

    public LatLng getPosition(){
        return position;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setRating(Double rating){
        this.rating = rating;
    }

    public Double getRating(){
        return rating;
    }

    public void setFeature1(Boolean feature1){
        this.feature1 = feature1;
    }

    public Boolean getFeature1(){
        return feature1;
    }

    public void setFeature2(Boolean feature2){
        this.feature2 = feature2;
    }

    public Boolean getFeature2(){
        return feature2;
    }

    public void setFeature3(Boolean feature3){
        this.feature3 = feature3;
    }

    public Boolean getFeature3(){
        return feature3;
    }

    public void setFeature4(Boolean feature4){
        this.feature4 = feature4;
    }

    public Boolean getFeature4(){
        return feature4;
    }

    public void setFeature5(Boolean feature5){
        this.feature5 = feature5;
    }

    public Boolean getFeature5(){
        return feature5;
    }

    public void setFeature6(Boolean feature6){
        this.feature6 = feature6;
    }

    public Boolean getFeature6(){
        return feature6;
    }

    public void setFeature7(Boolean feature7){
        this.feature7 = feature7;
    }

    public Boolean getFeature7(){
        return feature7;
    }

    public void setFeature8(Boolean feature8){
        this.feature8 = feature8;
    }

    public Boolean getFeature8(){
        return feature8;
    }

    public void setFeature9(Boolean feature9){
        this.feature9 = feature9;
    }

    public Boolean getFeature9(){
        return feature9;
    }

    public void setFeature10(Boolean feature10){
        this.feature10 = feature10;
    }

    public Boolean getFeature10(){
        return feature10;
    }

    public void setKnownSites(List<LatLng> knownSites){
        this.knownSites = knownSites;
    }

    public List<LatLng> getKnownSites(){
        return this.knownSites;
    }

    public void setSiteSize(int siteSize) {
        this.siteSize = siteSize;
    }

    public int getSiteSize(){
        return siteSize;
    }

    public void setUnknownSites(List<LatLng> unknownSites){
        this.unknownSites = unknownSites;
    }

    public List<LatLng> getUnknownSites(){
        return this.unknownSites;
    }

    public void setUnknownSitesSize(int unknownSitesSize) {
        this.unknownSitesSize = unknownSitesSize;
    }

    public int getUnknownSitesSize(){
        return unknownSitesSize;
    }
}

