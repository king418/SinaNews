package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.king.app.AppContext;
import com.king.configuration.Constants;

/**
 * AUTHOR: King
 * DATE: 2015/4/25.
 */
public class RegistActivity extends Activity {

    private ImageView img_regist_back;
    private Button btn_regist_next;
    private EditText et_regist_username;
    private EditText et_regist_psw;
    private EditText et_regist_code;
    private NetworkImageView img_regist_code;
    private ImageLoader imageLoader;
    private String sequence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        initData();
        addListener();
    }

    private void initView(){
        btn_regist_next = (Button) findViewById(R.id.btn_regist_next);
        img_regist_back = (ImageView) findViewById(R.id.img_regist_back);
        et_regist_code = (EditText) findViewById(R.id.et_regist_code);
        et_regist_psw = (EditText) findViewById(R.id.et_regist_psw);
        et_regist_username = (EditText) findViewById(R.id.et_regist_username);
        img_regist_code = (NetworkImageView) findViewById(R.id.img_regist_code);
    }
    
    private void initData(){
        sequence = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        AppContext instance = AppContext.getInstance();
        imageLoader = instance.getImageLoader();
        img_regist_code.setImageUrl(Constants.CODE_URL+ sequence,imageLoader);
    }

    private void addListener(){
        img_regist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_regist_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_regist_username.getText().toString().trim();
                String psw = et_regist_psw.getText().toString().trim();
                String code = et_regist_code.getText().toString().trim();
                if (!username.equals("")||!psw.equals("")||!code.equals("")){
                    Intent intent = new Intent(RegistActivity.this,RegistNextActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username);
                    bundle.putString("psw",psw);
                    bundle.putString("code",code);
                    bundle.putString("sequence",sequence);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegistActivity.this,"不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}