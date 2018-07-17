package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous51 on 27/9/17.
 */

public class CategoryListResponse {

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("count")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;
}
