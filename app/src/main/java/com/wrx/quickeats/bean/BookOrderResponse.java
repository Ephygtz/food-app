package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 19/1/18.
 */

public class BookOrderResponse {

    @SerializedName("order_no")
    private String order_no;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
