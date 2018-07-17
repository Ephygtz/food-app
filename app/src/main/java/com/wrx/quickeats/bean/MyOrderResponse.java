package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mobulous51 on 5/10/17.
 */

public class MyOrderResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("rest_name")
    private String rest_name;

    @SerializedName("rest_type")
    private String rest_type;

    @SerializedName("image_url")
    private String image_url;

    @SerializedName("total_item")
    private String total_item;

    @SerializedName("total_amount")
    private String total_amount;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    @SerializedName("accept_time")
    private String accept_time;

    @SerializedName("dispatch_time")
    private String dispatch_time;

    @SerializedName("delivery_time")
    private String delivery_time;

    @SerializedName("items")
    private List<OrderSummryResponse> orderSummryResponseList;

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_type() {
        return rest_type;
    }

    public void setRest_type(String rest_type) {
        this.rest_type = rest_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getDispatch_time() {
        return dispatch_time;
    }

    public void setDispatch_time(String dispatch_time) {
        this.dispatch_time = dispatch_time;
    }

    public List<OrderSummryResponse> getOrderSummryResponseList() {
        return orderSummryResponseList;
    }

    public void setOrderSummryResponseList(List<OrderSummryResponse> orderSummryResponseList) {
        this.orderSummryResponseList = orderSummryResponseList;
    }


}
