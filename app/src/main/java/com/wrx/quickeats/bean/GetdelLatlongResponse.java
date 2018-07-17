package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 29/1/18.
 */

public class GetdelLatlongResponse {

    @SerializedName("rest_lat")
    private String rest_lat;

    @SerializedName("rest_long")
    private String rest_long;

    @SerializedName("del_lat")
    private String del_lat;

    @SerializedName("del_long")
    private String del_long;


    public String getRest_lat() {
        return rest_lat;
    }

    public void setRest_lat(String rest_lat) {
        this.rest_lat = rest_lat;
    }

    public String getRest_long() {
        return rest_long;
    }

    public void setRest_long(String rest_long) {
        this.rest_long = rest_long;
    }

    public String getDel_lat() {
        return del_lat;
    }

    public void setDel_lat(String del_lat) {
        this.del_lat = del_lat;
    }

    public String getDel_long() {
        return del_long;
    }

    public void setDel_long(String del_long) {
        this.del_long = del_long;
    }



}
