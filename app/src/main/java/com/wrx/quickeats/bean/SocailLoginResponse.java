package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous51 on 25/9/17.
 */

public class SocailLoginResponse {

    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("user_image")
    private String user_image;

    @SerializedName("fb_id")
    private String fb_id;

    @SerializedName("g_id")
    private String g_id;

    @SerializedName("mobile_number")
    private String mobile_number;

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

}
