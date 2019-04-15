package com.cseaeventmanagement;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {
    private static Singleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private Singleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new Singleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

// Getting a RequestQueue
//RequestQueue queue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();
//Singleton.getInstance(this).addToRequestQueue(jor);
