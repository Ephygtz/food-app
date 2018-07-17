package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mobulous51 on 30/8/17.
 */

public class CommonResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("requestKey")
    private String requestKey;

    @SerializedName("txncd")
    private String txncd="";

    @SerializedName("text")
    private String text="";

    @SerializedName("login")
    private LoginResponse login;

    @SerializedName("socialLogin")
    private SocailLoginResponse socialLogin;

    @SerializedName("categoryList")
    private List<CategoryListResponse> categoryList;

    @SerializedName("menuList")
    private List<MenuResponse> menuList;

    @SerializedName("home")
    private List<HomeResponse> home;

    public List<HomeResponse> getHome() {
        return home;
    }

    public void setHome(List<HomeResponse> home) {
        this.home = home;
    }

    @SerializedName("verificationCode")
    private String verificationCode;

    @SerializedName("myFavourites")
    private List<MyFavouriteResponse> myFavourites;

    @SerializedName("profile")
    private ProfileResponse profile;

    @SerializedName("register")
    private RegisterResponse register;

    @SerializedName("categoryMenu")
    private List<MenuResponse> categoryMenu;

    @SerializedName("myOrders")
    private List<MyOrderResponse> myOrders;

    @SerializedName("favourite")
    private FavouriteResponse favourite;

    @SerializedName("searchType")
    private List<SearchTypeResponse> searchType;

    @SerializedName("notification")
    private List<NotificationResponse> notification;

    @SerializedName("bookOrder")
    private BookOrderResponse orderResponse;

    @SerializedName("data")
    private MPesaResponse mPesaResponse;

    @SerializedName("getdelLatlong")
    private GetdelLatlongResponse LatlongResponse;

    public GetdelLatlongResponse getLatlongResponse() {
        return LatlongResponse;
    }

    public void setLatlongResponse(GetdelLatlongResponse latlongResponse) {
        LatlongResponse = latlongResponse;
    }

    public BookOrderResponse getOrderResponse() {
        return orderResponse;
    }

    public void setOrderResponse(BookOrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    public String getTxncd() {
        return txncd;
    }

    public void setTxncd(String txncd) {
        this.txncd = txncd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<RestaurantResponse> getRestDetail() {
        return restDetail;
    }

    public void setRestDetail(List<RestaurantResponse> restDetail) {
        this.restDetail = restDetail;
    }

    @SerializedName("searchDetail")
    private List<RestaurantResponse> searchDetail;

    public List<RestaurantResponse> getSearchDetail() {
        return searchDetail;
    }

    public void setSearchDetail(List<RestaurantResponse> searchDetail) {
        this.searchDetail = searchDetail;
    }

    @SerializedName("restDetail")
    private List<RestaurantResponse> restDetail;

    public List<SearchTypeResponse> getSearchType() {
        return searchType;
    }

    public void setSearchType(List<SearchTypeResponse> searchType) {
        this.searchType = searchType;
    }

    public FavouriteResponse getFavourite() {
        return favourite;
    }

    public void setFavourite(FavouriteResponse favourite) {
        this.favourite = favourite;
    }

    public ProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(ProfileResponse profile) {
        this.profile = profile;
    }

    public List<MyOrderResponse> getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(List<MyOrderResponse> myOrders) {
        this.myOrders = myOrders;
    }

    public List<MenuResponse> getCategoryMenu() {
        return categoryMenu;
    }

    public void setCategoryMenu(List<MenuResponse> categoryMenu) {
        this.categoryMenu = categoryMenu;
    }
    public List<MyFavouriteResponse> getMyFavourites() {
        return myFavourites;
    }

    public void setMyFavourites(List<MyFavouriteResponse> myFavourites) {
        this.myFavourites = myFavourites;
    }

    public SocailLoginResponse getSocialLogin() {
        return socialLogin;
    }

    public void setSocialLogin(SocailLoginResponse socialLogin) {
        this.socialLogin = socialLogin;
    }

    public List<CategoryListResponse> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListResponse> categoryList) {
        this.categoryList = categoryList;
    }

    public List<MenuResponse> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuResponse> menuList) {
        this.menuList = menuList;
    }


    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LoginResponse getLogin() {
        return login;
    }

    public void setLogin(LoginResponse login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public RegisterResponse getRegister() {
        return register;
    }

    public void setRegister(RegisterResponse register) {
        this.register = register;
    }

    public List<NotificationResponse> getNotification() {
        return notification;
    }

    public void setNotification(List<NotificationResponse> notification) {
        this.notification = notification;
    }

    public MPesaResponse getmPesaResponse() {
        return mPesaResponse;
    }

    public void setmPesaResponse(MPesaResponse mPesaResponse) {
        this.mPesaResponse = mPesaResponse;
    }
}
