package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous51 on 27/10/17.
 */

public class RestaurantResponse {

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

    @SerializedName("rest_description")
    private String rest_description;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("type")
    private String type;

    @SerializedName("image_url")
    private String image_url;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getRest_location() {
        return rest_location;
    }

    public void setRest_location(String rest_location) {
        this.rest_location = rest_location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getRest_type() {
        return rest_type;
    }

    public void setRest_type(String rest_type) {
        this.rest_type = rest_type;
    }

    public String getRest_description() {
        return rest_description;
    }

    public void setRest_description(String rest_description) {
        this.rest_description = rest_description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


}
