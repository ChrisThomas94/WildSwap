package scot.wildcamping.wildscotland.model;

/**
 * Created by Chris on 19-Mar-16.
 */
public class Gallery {

    private String image1;
    private String image2;
    private String image3;
    private String cid;

    public void setImage1(String image1){
        this.image1 = image1;
    }

    public String getImage1(){
        return image1;
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

    public void setCid(String cid){
        this.cid = cid;
    }

    public String getCid(){
        return cid;
    }
}
