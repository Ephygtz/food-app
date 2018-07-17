package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mobulous51 on 22/9/17.
 */

public class HomeResponse  implements Serializable{

    @SerializedName("id")
    private String id;

    @SerializedName("owner_id")
    private String owner_id;

    @SerializedName("rest_name")
    private String rest_name;

    @SerializedName("rest_image")
    private String rest_image;

    @SerializedName("rest_location")
    private String rest_location;

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

    @SerializedName("created_on")
    private String created_on;

    @SerializedName("updated_on")
    private String updated_on;

    @SerializedName("distance")
    private String distance;

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

    public String getRest_image() {
        return rest_image;
    }

    public void setRest_image(String rest_image) {
        this.rest_image = rest_image;
    }

    public String getRest_location() {
        return rest_location;
    }

    public void setRest_location(String rest_location) {
        this.rest_location = rest_location;
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

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }




}
