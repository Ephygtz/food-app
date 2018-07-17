package com.wrx.quickeats.retrofit;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestApi {
    public static final String BASE_URL = "https://ekr7eb9nl4.execute-api.us-west-2.amazonaws.com/V1/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    // set your desired log level
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static Retrofit.Builder retrofitBuilder = new Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));


//    public static <S> S createService(Class<S> aClass){
//        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
//        return retrofit.create(aClass);
//    }

    public static <S> S createService(Class<S> aClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("x-api-key", "qfsmWWQsUH7BRD5llP2H270zX1fXTBmG6eU5fWQP").
                        header("Content-Type","application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                return response;
            }
        });

        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
        return retrofit.create(aClass);
    }


    //for the update api




    public static RequestBody getRequestBody(String params) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), params);
    }

}
