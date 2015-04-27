package com.king.app;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.king.model.UserInfo;
import com.king.utils.MemoryCache;

/**
 * Created by Administrator on 2015/4/21.
 */
public class AppContext extends Application {

    private static AppContext appContext;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private UserInfo userInfo;
    private boolean isLogin;

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

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            getRequestQueue();
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 8);
            imageLoader = new ImageLoader(requestQueue, new MemoryCache(maxMemory));
        }
        return imageLoader;
    }

    public UserInfo getUserInfo(){
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    public boolean isLogin(){
        if (userInfo == null){
            return false;
        }
        return true;
    }

}
