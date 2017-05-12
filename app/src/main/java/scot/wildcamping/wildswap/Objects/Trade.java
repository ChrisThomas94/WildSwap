package scot.wildcamping.wildswap.Objects;

/**
 * Created by Chris on 12-Mar-16.
 */
public class Trade {

    private String unique_tid;
    private String sender_uid;
    private String reciever_uid;
    private String send_cid;
    private String recieve_cid;
    private String date;
    private String userRelation;
    private int status;

    public void setUnique_tid(String unique_tid){
        this.unique_tid = unique_tid;
    }

    public String getUnique_tid(){
        return unique_tid;
    }

    public void setSender_uid(String sender_uid){
        this.sender_uid = sender_uid;
    }

    public String getSender_uid(){
        return sender_uid;
    }

    public void setReciever_uid(String reciever_uid){
        this.reciever_uid = reciever_uid;
    }

    public String getReciever_uid(){
        return reciever_uid;
    }

    public void setSend_cid(String send_cid){
        this.send_cid = send_cid;
    }

    public String getSend_cid(){
        return send_cid;
    }

    public void setRecieve_cid(String recieve_cid){
        this.recieve_cid = recieve_cid;
    }

    public String getRecieve_cid(){
        return recieve_cid;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setUserRelation(String relat){
        this.userRelation = relat;
    }

    public String getUserRelation(){
        return userRelation;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

}
