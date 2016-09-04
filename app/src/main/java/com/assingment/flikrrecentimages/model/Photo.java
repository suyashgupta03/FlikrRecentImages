package com.assingment.flikrrecentimages.model;

/**
 * Created by suyashg on 03/09/16.
 */

public class Photo {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private long farm;
    private String title;
    private int ispublic;
    private int isfriend;
    private int isfamily;
    private String url_n;
    private String height_n;
    private int width_n;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getFarm() {
        return farm;
    }

    public void setFarm(long farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIspublic() {
        return ispublic;
    }

    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

    public String getUrl_n() {
        return url_n;
    }

    public void setUrl_n(String url_n) {
        this.url_n = url_n;
    }

    public String getHeight_n() {
        return height_n;
    }

    public void setHeight_n(String height_n) {
        this.height_n = height_n;
    }

    public int getWidth_n() {
        return width_n;
    }

    public void setWidth_n(int width_n) {
        this.width_n = width_n;
    }
}
