package scot.wildcamping.wildscotland.model;

/**
 * Created by Chris on 10-Apr-16.
 */
public class User {

    private String name;
    private String email;
    private String bio;

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
