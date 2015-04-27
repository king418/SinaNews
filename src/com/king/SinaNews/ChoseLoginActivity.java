package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * AUTHOR: King
 * DATE: 2015/4/25.
 */
public class ChoseLoginActivity extends Activity {

    private Button btn_choselogin_regist;
    private Button btn_choselogin_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choselogin);
        initView();
        addListener();
    }

    private void initView() {
        btn_choselogin_login = (Button) findViewById(R.id.btn_choselogin_login);
        btn_choselogin_regist = (Button) findViewById(R.id.btn_choselogin_regist);
    }

    private void addListener(){
        btn_choselogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoseLoginActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_choselogin_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoseLoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }

}