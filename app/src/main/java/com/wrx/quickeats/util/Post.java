package com.wrx.quickeats.util;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Post {

  public static void PostData(String url, JSONObject parameter, Response.Listener<JSONObject> response) {

    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, parameter,
        response, new Error()) {

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        headers.put("Content-Type", "application/json; charset=utf-8");
        //                ; charset=utf-8

        //                String creds = String.format("%s:%s", "username", "pass");
        String auth = "Bearer 708dff7744dd4b3e8577f1527ee3c354" ;
        headers.put("Authorization", auth);
        return headers;
      }

      @Override
      protected Map<String, String> getParams() {
        // Posting params to register url
        Map<String, String> params = new HashMap<String, String>();
        //                Log.e("category id", getIntent().getStringExtra("category_id"));
        //                params.put("categoryId", 2 + "");


        return params;
      }
    };
    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(req);
    Log.e("request is", req.toString());

  }

  private static class Error implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
      NetworkResponse response = error.networkResponse;
      if (error instanceof ServerError && response != null) {
        try {
          String res = new String(response.data,
              HttpHeaderParser.parseCharset(response.headers, "utf-8"));
          // Now you can use any deserializer to make sense of data
          JSONObject obj = new JSONObject(res);
          Log.e("obj", obj.toString());
        } catch (UnsupportedEncodingException e1) {
          // Couldn't properly decode data to string
          Log.e("e1", e1.toString());
          e1.printStackTrace();
        } catch (JSONException e2) {
          // returned data is not JSONObject?
          e2.printStackTrace();
          Log.e("e2", e2.toString());
        }
      }

    }
  }
}
