package com.king.SinaNews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.UserInfo;
import com.king.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * AUTHOR: King
 * DATE: 2015/4/25.
 */
public class LoginActivity extends Activity {

    private ImageView img_login_back;
    private EditText et_login_username;
    private EditText et_login_psw;
    private Button btn_login_login;
    private TextView tv_login_losspsw;
    private RequestQueue requestQueue;
    private String login_url;
    private UserInfo userInfo;
    private AppContext appContext;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        img_login_back = (ImageView) findViewById(R.id.img_login_back);
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_psw = (EditText) findViewById(R.id.et_login_psw);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);
        tv_login_losspsw = (TextView) findViewById(R.id.tv_login_forgetpsw);
    }

    private void initData() {
        requestQueue = AppContext.getInstance().getRequestQueue();
        appContext = AppContext.getInstance();
        login_url = Constants.DO_LOGIN;
    }

    private void addListener() {
        img_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_login_losspsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LossPswActivity.class);
                startActivity(intent);
            }
        });
        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = et_login_username.getText().toString().trim();
                String psw = et_login_psw.getText().toString().trim();
                if (username.equals("") || psw.equals("")) {
                    Toast.makeText(appContext, "用户名，密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    SharedPreferences preferences = getSharedPreferences("logininfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username",username);
                    editor.putString("password",psw);
                    editor.commit();
                    postUseLogin(username, psw);
                }
            }
        });
    }

    private void postUseLogin(final String username, final String psw) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        userInfo = JsonUtils.parseUser(response);
                        if (userInfo != null) {
                            appContext.setUserInfo(userInfo);
                            finish();
                            Toast.makeText(appContext, "登陆成功", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = JsonUtils.parseMessage(response);
                            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(appContext, "数据异常！！", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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