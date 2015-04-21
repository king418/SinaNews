package com.king.app;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2015/4/21.
 */
public class AppContext extends Application {

    private static AppContext appContext;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        firstCreateInstance();
    }

    protected void firstCreateInstance() {
        if (appContext == null) {
            appContext = this;
        }
    }

    public static AppContext getInstance() {
        return appContext;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }


}
