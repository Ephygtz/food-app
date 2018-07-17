package com.wrx.quickeats.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mobua06 on 16/6/17.
 */

public class ApiClient {

  //    public static final String BASE_URL = "http://mobulous.co.in/restaurent/CustomerServices/";
  public static final String BASE_URL = "http://18.217.198.61/restaurent/CustomerServices/";
  public static final String BASE_URL_PAYMENT = "https://apis.ipayafrica.com/payments/v2/";
  //public static final String BASE_URL = "https://ekr7eb9nl4.execute-api.us-west-2.amazonaws.com/V1/";

  private static Retrofit retrofit = null;
  private static Retrofit payment_retrofit = null;

  public static Retrofit getClient() {
    Gson gson = new GsonBuilder().setLenient().create();

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(80, TimeUnit.SECONDS)
        .readTimeout(80, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build();

    if (retrofit == null) {
      retrofit = new Retrofit.Builder().client(client)
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
    }
    return retrofit;
  }

  public static Retrofit getClientPaymentMpesa() {
    Gson gson = new GsonBuilder().setLenient().create();

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(80, TimeUnit.SECONDS)
        .readTimeout(80, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build();

    if (payment_retrofit == null) {
      payment_retrofit = new Retrofit.Builder().client(client)
          .baseUrl(BASE_URL_PAYMENT)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
    }
    return payment_retrofit;
  }
}
