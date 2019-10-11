package com.zzq.app.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzq.app.app.App;

import java.util.HashMap;
import java.util.Map;

import static com.zzq.app.network.MyRequest.TAG;

public class RequestManager {
    private RequestQueue queue;
    private static volatile RequestManager instance;

    private RequestManager(){
        queue = Volley.newRequestQueue(App.getInstance());
    }

    public static RequestManager getInstance(){
        if (instance == null) {
            synchronized (RequestManager.class){
                if (instance == null){
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return queue;
    }

    public <T> void sendGet(String url, Class<T> clazz, final MyListener<T> listener){
        MyRequest<T> request = new MyRequest<T>(url, clazz, new Response.Listener<T>(){
            @Override
            public void onResponse(T response){
                Log.d(TAG, "Response.Listener<>.onResponse() called.");
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Response.ErrorListener() called.");
                listener.onError(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                headers.put("api-key", "LO79mBj364HBhD1ZuPNPEDiMMAw=");
                return headers;
            }
        };
        addToRequestQueue(request);
    }

    public <T> void sendPost(String url, Class<T> clazz, final HashMap<String, String> map, final MyListener<T> listener){
        MyRequest<T> request = new MyRequest<T>(Request.Method.POST, url, clazz, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        addToRequestQueue(request);
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
