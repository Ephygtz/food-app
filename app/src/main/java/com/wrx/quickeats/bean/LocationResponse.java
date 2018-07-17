package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous51 on 27/10/17.
 */

public class LocationResponse {

    @SerializedName("city")
    private String city;

    @SerializedName("restaurent_id")
    private String restaurent_id;

    @SerializedName("type")
    private String type;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRestaurent_id() {
        return restaurent_id;
    }

    public void setRestaurent_id(String restaurent_id) {
        this.restaurent_id = restaurent_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
