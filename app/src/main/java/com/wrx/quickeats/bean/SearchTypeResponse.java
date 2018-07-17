package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mobulous51 on 27/10/17.
 */

public class SearchTypeResponse {

    @SerializedName("location")
    private List<LocationResponse> location;

    @SerializedName("restaurent")
    private List<RestaurantResponse> restaurent;

    @SerializedName("cousine")
    private String cousine;

    public List<LocationResponse> getLocation() {
        return location;
    }

    public void setLocation(List<LocationResponse> location) {
        this.location = location;
    }


    public List<RestaurantResponse> getRestaurent() {
        return restaurent;
    }

    public void setRestaurent(List<RestaurantResponse> restaurent) {
        this.restaurent = restaurent;
    }

    public String getCousine() {
        return cousine;
    }

    public void setCousine(String cousine) {
        this.cousine = cousine;
    }


}
