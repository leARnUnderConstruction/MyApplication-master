package com.example.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static  MySingleton mInstance;
    private RequestQueue mrequesQueue;
    private static Context mctx;

    private MySingleton(Context context)
    {
        mctx =context;
        mrequesQueue =getRequestqueue();
    }

    private RequestQueue getRequestqueue() {

        if(mrequesQueue ==null)
        {
            mrequesQueue = Volley.newRequestQueue(mctx.getApplicationContext());

        }

        return mrequesQueue;
    }


    public static synchronized MySingleton getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance =new MySingleton(context);
        }
        return  mInstance;
    }


    public <T> void addToRequestqueue(Request<T> request)
    {
        mrequesQueue.add(request);
    }

}
