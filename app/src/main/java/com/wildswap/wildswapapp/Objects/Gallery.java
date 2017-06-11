package com.wildswap.wildswapapp.Objects;

import java.util.ArrayList;

/**
 * Created by Chris on 19-Mar-16.
 *
 */
public class Gallery {

    private String cid;
    private ArrayList<String> gallery;
    private Boolean hasGallery;

    public void setHasGallery(Boolean hasGallery){
        this.hasGallery = hasGallery;
    }

    public Boolean getHasGallery() {
        return hasGallery;
    }

    public void setGallery(ArrayList<String> images){
        this.gallery = images;
    }

    public ArrayList<String> getGallery(){
        return gallery;
    }

    public void setCid(String cid){
        this.cid = cid;
    }

    public String getCid(){
        return cid;
    }

}
