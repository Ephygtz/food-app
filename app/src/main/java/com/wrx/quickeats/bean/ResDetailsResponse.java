package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous51 on 28/10/17.
 */

public class ResDetailsResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("owner_id")
    private String owner_id;

    @SerializedName("rest_name")
    private String rest_name;

    @SerializedName("delivery_time")
    private String delivery_time;

    @SerializedName("rest_location")
    private String rest_location;

    @SerializedName("city")
    private String city;

    @SerializedName("zipcode")
    private String zipcode;

    @SerializedName("rest_type")
    private String rest_type;
}
