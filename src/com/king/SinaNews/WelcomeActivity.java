package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.UserInfo;
import com.king.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AUTHOR: King
 * DATE: 2015/4/26.
 */
public class WelcomeActivity extends Activity {

    private String username;
    private String password;

    private AppContext appContext;
    private RequestQueue requestQueue;
    private UserInfo userInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initData();

    }

    private void initData(){
        SharedPreferences preferences = getSharedPreferences("logininfo",MODE_PRIVATE);
        username = preferences.getString("username","");
        password = preferences.getString("password","");
        appContext = AppContext.getInstance();
        requestQueue = appContext.getRequestQueue();
        postUseLogin(username,password);
    }

    private void postUseLogin(final String username, final String psw) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DO_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        userInfo = JsonUtils.parseUser(response);
                        if (userInfo != null) {
                            appContext.setUserInfo(userInfo);
                        }
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },3000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", username);
                map.put("password", psw);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}