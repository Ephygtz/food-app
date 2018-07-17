package com.wrx.quickeats.retrofit;

import com.wrx.quickeats.bean.CommonResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by mobua06 on 16/6/17.
 */

public interface ApiInterface {

  @FormUrlEncoded @POST("register") Call<CommonResponse> getSingupResult(
      @Field("email") String email, @Field("mobile_number") String phone,
      @Field("password") String pass, @Field("device_type") String device_type,
      @Field("device_token") String device_token);

  @FormUrlEncoded @POST("chkUser") Call<CommonResponse> getchkUserResult(
      @Field("email") String email, @Field("mobile_number") String phone);

  @FormUrlEncoded @POST("verificationCode") Call<CommonResponse> getverificationCodeResult(
      @Field("mobile_number") String phone);

  @FormUrlEncoded @POST("login") Call<CommonResponse> getLoginResult(@Field("email") String email,
      @Field("password") String pasword, @Field("device_type") String device_type,
      @Field("device_token") String device_token);

  @FormUrlEncoded @POST("socialLogin") Call<CommonResponse> getSocialLoginResult(
      @Field("name") String name, @Field("email") String email,
      @Field("social_id") String social_id, @Field("image") String image,
      @Field("type") String fbOrgoogle, @Field("device_type") String device_type,
      @Field("device_token") String device_token);

  @FormUrlEncoded @POST("forgotPassword") Call<CommonResponse> getForgotResult(
      @Field("email") String email);

  @FormUrlEncoded @POST("resetPassword") Call<CommonResponse> getResetPasswordResult(
      @Field("otp") String token, @Field("newPassword") String newPassword,
      @Field("cnfPassword") String cnfPassword);

  @FormUrlEncoded @POST("changePassword") Call<CommonResponse> getChangePasswordResult(
      @Field("token") String token, @Field("password") String newPassword);

  @FormUrlEncoded @POST("logout") Call<CommonResponse> getLogoutResult(
      @Field("token") String token);

  @FormUrlEncoded @POST("notification") Call<CommonResponse> getNotification(
      @Field("token") String token);

  //    @FormUrlEncoded
  //    @POST("profile")
  //    Call<CommonResponse> getProfileResult(@Field("token") String token,
  //                                          @Field("name") String name,
  //                                          @Field("email") String email,
  //                                          @Field("mobile_number") String mobile_number,
  //                                          @Field("address") String address,
  //                                          @Field("image") String image);

  @FormUrlEncoded @POST("home")
    //response name must be as it is i.e of response type.here in response,name "home" comes of array type.inside array home, a lot of item are comeing
  Call<CommonResponse> getHomeResult(@Field("token") String token,
      //parameter names which we have to pass
      @Field("latitude") String latitude, @Field("longitude") String longitude);

  @FormUrlEncoded @POST("menuList") Call<CommonResponse> getMenuListResult(
      @Field("token") String token, @Field("restaurent_id") String restaurent_id);

  @FormUrlEncoded @POST("categoryList") Call<CommonResponse> getCategoryListResult(
      @Field("token") String token, @Field("rest_id") String restaurent_id);

  @FormUrlEncoded @POST("categoryMenu") Call<CommonResponse> getCategoryMenuResult(
      @Field("token") String token, @Field("rest_id") String restaurent_id,
      @Field("category") String category);

  @POST("bookOrder") Call<CommonResponse> getBookOrderResult(@Body RequestBody object);

  @FormUrlEncoded @POST("myOrders") Call<CommonResponse> getMyOrderResult(
      @Field("token") String token);

  @FormUrlEncoded @POST("myFavourites") Call<CommonResponse> getMyFavouriteResult(
      @Field("token") String token);

  @FormUrlEncoded @POST("favourite") Call<CommonResponse> getFavouriteResult(
      @Field("token") String token, @Field("restaurent_id") String restaurent_id,
      @Field("type") String type);

  @FormUrlEncoded @POST("searchType") Call<CommonResponse> getsearchTypeResult(
      @Field("token") String token, @Field("input") String input);

  @FormUrlEncoded @POST("searchDetail") Call<CommonResponse> getsearchDetailResult(
      @Field("token") String token, @Field("restaurent_id") String restaurent_id,
      @Field("type") String type, @Field("lat") String lat, @Field("long") String lon,
      @Field("city") String city);

  @FormUrlEncoded @POST("restDetail") Call<CommonResponse> getrestDetailResult(
      @Field("token") String token, @Field("restaurent_id") String restaurent_id);

  @FormUrlEncoded @POST("contact") Call<CommonResponse> getContactResult(
      @Field("token") String token, @Field("type") String type, @Field("title") String title,
      @Field("description") String description);

  @FormUrlEncoded @POST("changeStatus") Call<CommonResponse> getchangeStatusResult(
      @Field("token") String token, @Field("status") String status);

  @FormUrlEncoded @POST("payment") Call<CommonResponse> payment(@Field("token") String token,
      @Field("order_id") String order_id, @Field("type") String type,    //{COD,MPESA,CREDIT CARD}
      @Field("transaction_id") String transaction_id, @Field("payment_mode") String payment_mode,
      //{MPESA,CREDIT CARD}
      @Field("payment_date") String payment_date);

  @FormUrlEncoded @POST("transact") Call<CommonResponse> transact(@Field("live") String live,
      @Field("oid") String oid, @Field("inv") String inv, @Field("amount") String amount,
      @Field("tel") String tel, @Field("eml") String eml, @Field("vid") String vid,
      @Field("curr") String curr, @Field("cst") String cst, @Field("cbk") String cbk,
      @Field("hash") String hash);

  @FormUrlEncoded @POST("transact/push/mpesa") Call<CommonResponse> getMpessa(
      @Field("phone") String phone, @Field("sid") String sid, @Field("vid") String vid,
      @Field("hash") String hash);

  @FormUrlEncoded @POST("transact/mobilemoney") Call<CommonResponse> transactMobileMoney(
      @Field("sid") String sid, @Field("vid") String vid, @Field("hash") String hash);

  @FormUrlEncoded @POST("transact/cc") Call<CommonResponse> transactCreditCard(
      @Field("sid") String sid, @Field("vid") String vid, @Field("cardno") String cardno,
      @Field("cvv") String cvv, @Field("month") String month, @Field("year") String year,
      @Field("curr") String curr, @Field("cust_address") String cust_address,
      @Field("cust_city") String cust_city, @Field("cust_country") String cust_country,
      @Field("cust_postcode") String cust_postcode, @Field("cust_stateprov") String cust_stateprov,
      @Field("fname") String fname, @Field("lname") String lname, @Field("hash") String hash);

  @FormUrlEncoded @POST("getdelLatlong") Call<CommonResponse> getdelLatlong(
      @Field("token") String token, @Field("order_id") String order_id,
      @Field("restaurent_id") String restaurent_id);

  @Multipart @POST("profile") Call<CommonResponse> getProfileResult(
      @PartMap Map<String, RequestBody> partMapData, @Part MultipartBody.Part profile);
}
