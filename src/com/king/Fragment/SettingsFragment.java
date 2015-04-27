package com.king.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.king.SinaNews.ChoseLoginActivity;
import com.king.SinaNews.CollectActivity;
import com.king.SinaNews.MyCommentActivity;
import com.king.SinaNews.R;
import com.king.app.AppContext;
import com.king.model.UserInfo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * AUTHOR: King
 * DATE: 2015/4/24.
 */
public class SettingsFragment extends Fragment {

    private View view;
    private AppContext appContext;
    private Button btn_setting_head;
    private Button btn_setting_logoff;
    private boolean isLogin;

    private UserInfo userInfo;
    private TextView tv_setting_username;
    private TextView tv_setting_collect;
    private TextView tv_setting_comment;
    private LinearLayout ll_contain_admin;
    private LinearLayout ll_container_size;
    private LinearLayout ll_contain_clear;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        btn_setting_head = (Button) view.findViewById(R.id.btn_setting_head);
        tv_setting_username = (TextView) view.findViewById(R.id.tv_setting_username);
        btn_setting_logoff = (Button) view.findViewById(R.id.btn_setting_logoff);
        tv_setting_collect = (TextView) view.findViewById(R.id.tv_setting_collect);
        tv_setting_comment = (TextView) view.findViewById(R.id.tv_setting_comment);
        ll_contain_admin = (LinearLayout) view.findViewById(R.id.ll_container_admin);
        ll_container_size = (LinearLayout) view.findViewById(R.id.ll_container_size);
        ll_contain_clear = (LinearLayout) view.findViewById(R.id.ll_container_clear);
        if (userInfo != null) {
            tv_setting_username.setText(userInfo.getUsername());
        }
        addListener();
        return view;
    }

    private void initData() {
        appContext = AppContext.getInstance();
        userInfo = appContext.getUserInfo();
        isLogin = appContext.isLogin();
    }

    private void addListener() {
        btn_setting_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo == null) {
                    Intent intent = new Intent(getActivity(), ChoseLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn_setting_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo != null) {
                    userInfo = null;
                    appContext.setUserInfo(userInfo);
                    tv_setting_username.setText("登录账号");
                    SharedPreferences preferences = appContext.getSharedPreferences("logininfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", "");
                    editor.putString("password", "");
                    editor.commit();
                    Toast.makeText(appContext, "退出登录", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(appContext, "您尚未登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_setting_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin){
                    Intent intent = new Intent(appContext, CollectActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(appContext, "您尚未登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_setting_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin){
                    Intent intent = new Intent(appContext, MyCommentActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(appContext, "您尚未登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll_contain_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(appContext, "功能建设中", Toast.LENGTH_SHORT).show();
            }
        });
        ll_container_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(appContext, "功能建设中", Toast.LENGTH_SHORT).show();
            }
        });
        ll_contain_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("正在清除缓存。。");
                dialog.show();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(appContext, "清除完成", Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = appContext.getUserInfo();
        isLogin = appContext.isLogin();
        if (tv_setting_username != null && userInfo != null) {
            tv_setting_username.setText(userInfo.getUsername());

        }
    }
}
