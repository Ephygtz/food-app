package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous51 on 27/9/17.
 */

public class CategoryMenuResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("owner_id")
    private String owner_id;

    @SerializedName("restaurent_id")
    private String restaurent_id;

    @SerializedName("menu_name")
    private String menu_name;

    @SerializedName("menu_category")
    private String menu_category;

    @SerializedName("menu_image")
    private String menu_image;

    @SerializedName("menu_price")
    private String menu_price;

    @SerializedName("menu_description")
    private String menu_description;

    @SerializedName("image_url")
    private String image_url;

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

    public String getRestaurent_id() {
        return restaurent_id;
    }

    public void setRestaurent_id(String restaurent_id) {
        this.restaurent_id = restaurent_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(String menu_category) {
        this.menu_category = menu_category;
    }

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_description() {
        return menu_description;
    }

    public void setMenu_description(String menu_description) {
        this.menu_description = menu_description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


}
