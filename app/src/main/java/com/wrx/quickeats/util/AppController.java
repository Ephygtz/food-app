package com.wrx.quickeats.util;

import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends  RestroApplication{

  public static final String TAG = AppController.class.getSimpleName();

  private RequestQueue mRequestQueue;
  private ImageLoader mImageLoader;

  private static AppController mInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    mInstance = this;
  }

  public static synchronized AppController getInstance() {
    return mInstance;
  }

  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    return mRequestQueue;
  }

  public ImageLoader getImageLoader() {
    getRequestQueue();
    if (mImageLoader == null) {
      mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
    }

    return this.mImageLoader;
  }

  public <T> void addToRequestQueue(Request<T> request, String tag) {
    //        set the default tag if tag is empty
    request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(request);
  }

  public <T> void addToRequestQueue(Request<T> request) {
    request.setTag(TAG);
    getRequestQueue().add(request);
  }

  public void cancelPendingRequests(Object tag) {
    if (mRequestQueue != null)
      mRequestQueue.cancelAll(tag);
  }


}
