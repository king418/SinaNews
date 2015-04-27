package com.king.SinaNews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.UserInfo;
import com.king.utils.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * AUTHOR: King
 * DATE: 2015/4/25.
 */
public class RegistNextActivity extends Activity {

    private String username;
    private String psw;
    private String verify_code;
    private String sequence;
    private String sex;
    private String nickname;
    private String submit_url;
    private RequestQueue requestQueue;

    private ImageView img_registnext_back;
    private ImageView img_registnext_upload;
    private EditText et_registnext_nickname;
    private EditText et_registnext_phone;
    private Button btn_registnext_complete;
    private RelativeLayout contain_regist_sex;
    private TextView tv_registnext_sex;
    private UserInfo userInfo;


    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registnext);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        img_registnext_back = (ImageView) findViewById(R.id.img_registnext_back);
        img_registnext_upload = (ImageView) findViewById(R.id.img_registnext_upload);
        et_registnext_nickname = (EditText) findViewById(R.id.et_registnext_nickname);
        et_registnext_phone = (EditText) findViewById(R.id.et_registnext_phone);
        btn_registnext_complete = (Button) findViewById(R.id.btn_rigistnext_complete);
        contain_regist_sex = (RelativeLayout) findViewById(R.id.contain_regist_sex);
        tv_registnext_sex = (TextView) findViewById(R.id.tv_registnext_sex);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        psw = bundle.getString("psw");
        verify_code = bundle.getString("code");
        sequence = bundle.getString("sequence");
        requestQueue = AppContext.getInstance().getRequestQueue();
        submit_url = Constants.DO_REGISTER;
    }

    private void addListener() {
        img_registnext_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_registnext_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegister();
            }
        });
        contain_regist_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] sexs = new String[]{"男", "女", "保密"};
                final String[] upSex = new String[]{"M", "W", ""};
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistNextActivity.this);
                builder.setItems(sexs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_registnext_sex.setText(sexs[which]);
                        sex = upSex[which];
                    }
                });
                builder.show();
            }
        });

        img_registnext_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistNextActivity.this);
                String[] item = new String[]{"从相册中获取","拍照"};
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                gallery();
                                break;
                            case 1:
                                camera();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }


    private void submitRegister() {
        submit_url = initUrl();
        StringRequest stringRequest = new StringRequest( submit_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("---->","----->"+submit_url);
                        getResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        requestQueue.add(stringRequest);
    }

    private void getResult(String jsonStr){
        userInfo = JsonUtils.parseUser(jsonStr);
        if (userInfo!=null){
            AppContext.getInstance().setUserInfo(userInfo);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        String message = JsonUtils.parseMessage(jsonStr);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private String initUrl(){
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.DO_REGISTER);
        sb.append("?username=").append(username);
        sb.append("&password=").append(psw);
        sb.append("&sequence=").append(sequence);
        sb.append("&verify_code=").append("");
        nickname = et_registnext_nickname.getText().toString();
        if (!nickname.equals("")) {
            try {
                nickname = URLEncoder.encode(nickname,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append("&nickname=").append(nickname);
        }
        if (sex != null && !sex.equals("")) {
            sb.append("&sex=").append(sex);
        }
        return sb.toString();
    }


    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

    }

    /*
     * 从相机获取
     */
    public void camera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(RegistNextActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                byte[] arr = Bitmap2Bytes(bitmap);
                this.img_registnext_upload.setImageBitmap(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}