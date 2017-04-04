package scot.wildcamping.wildscotland.Objects;

import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Chris on 05-Mar-16.
 */
public class Site {

    private String cid;
    private String token;
    private String siteAdmin;
    private LatLng position;
    private int popularity;
    private String title;
    private String description;
    private Double rating;
    private int ratedBy;
    private Double lat;
    private Double lon;

    private String classification;

    private List<android.location.Address> address;

    private SparseArray<String> features;

    private String display_pic;

    private String feature1;
    private String feature2;
    private String feature3;
    private String feature4;
    private String feature5;
    private String feature6;
    private String feature7;
    private String feature8;
    private String feature9;
    private String feature10;

    private String permission;
    private String distant;
    private String nearby;
    private String immediate;

    private String image;
    private String image2;
    private String image3;
    private SparseArray<Gallery> images;

    public String getDisplay_pic() {
        return display_pic;
    }

    public void setDisplay_pic(String display_pic) {
        this.display_pic = display_pic;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setAddress(List<android.location.Address> address){
        this.address = address;
    }

    public List<android.location.Address> getAddress(){
        return address;
    }

    public void setFeatures(SparseArray<String> features){
        this.features = features;
    }

    public SparseArray<String> getFeatures(){
        return features;
    }

    public void setLat(Double lat){
        this.lat = lat;
    }

    public Double getLat(){
        return lat;
    }

    public void setLon(Double lon){
        this.lon = lon;
    }

    public Double getLon(){
        return lon;
    }

    public void setCid(String cid){
        this.cid = cid;
    }

    public String getCid(){
        return cid;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public void setSiteAdmin(String admin){
        this.siteAdmin = admin;
    }

    public String getSiteAdmin(){
        return siteAdmin;
    }

    public void setPosition(LatLng position){
        this.position = position;
    }

    public LatLng getPosition(){
        return position;
    }

    public void setPopularity(int pop){
        this.popularity = pop;
    }

    public int getPopularity(){
        return popularity;
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

    public void setRatedBy(int ratedBy){
        this.ratedBy = ratedBy;
    }

    public int getRatedBy(){
        return ratedBy;
    }


    public void setFeature1(String feature1){
        this.feature1 = feature1;
    }

    public String getFeature1(){
        return feature1;
    }

    public void setFeature2(String feature2){
        this.feature2 = feature2;
    }

    public String getFeature2(){
        return feature2;
    }

    public void setFeature3(String feature3){
        this.feature3 = feature3;
    }

    public String getFeature3(){
        return feature3;
    }

    public void setFeature4(String feature4){
        this.feature4 = feature4;
    }

    public String getFeature4(){
        return feature4;
    }

    public void setFeature5(String feature5){
        this.feature5 = feature5;
    }

    public String getFeature5(){
        return feature5;
    }

    public void setFeature6(String feature6){
        this.feature6 = feature6;
    }

    public String getFeature6(){
        return feature6;
    }

    public void setFeature7(String feature7){
        this.feature7 = feature7;
    }

    public String getFeature7(){
        return feature7;
    }

    public void setFeature8(String feature8){
        this.feature8 = feature8;
    }

    public String getFeature8(){
        return feature8;
    }

    public void setFeature9(String feature9){
        this.feature9 = feature9;
    }

    public String getFeature9(){
        return feature9;
    }

    public void setFeature10(String feature10){
        this.feature10 = feature10;
    }

    public String getFeature10(){
        return feature10;
    }

    public void setPermission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }

    public void setDistant(String distant){
        this.distant = distant;
    }

    public String getDistant(){
        return distant;
    }

    public void setNearby(String nearby){
        this.nearby = nearby;
    }

    public String getNearby(){
        return nearby;
    }

    public void setImmediate(String immediate){
        this.immediate = immediate;
    }

    public String getImmediate(){
        return immediate;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public void setImage2(String image2){
        this.image2 = image2;
    }

    public String getImage2(){
        return image2;
    }

    public void setImage3(String image3){
        this.image3 = image3;
    }

    public String getImage3(){
        return image3;
    }

    public void setFetchedImages(SparseArray<Gallery> images){
        this.images = images;
    }

    public SparseArray<Gallery> getFetchedImages(){
        return images;
    }
}
