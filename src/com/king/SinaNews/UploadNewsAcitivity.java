package com.king.SinaNews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.king.adapter.GridAdapter;
import com.king.app.AppContext;
import com.king.configuration.Constants;
import com.king.model.UserInfo;
import com.king.model.activity.TextNewsDetail;
import com.king.utils.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AUTHOR: King
 * DATE: 2015/4/26.
 */
public class UploadNewsAcitivity extends Activity {

    private ImageView img_uploadnews_back;
    private Button btn_uploadnews_complete;
    private Spinner sp_uploadnews_cate;
    private EditText et_uploadnews_title;
    private EditText et_uploadnews_content;
    private GridView gv_uploadnews_img;

    private String[] titles = new String[]{"头条", "娱乐", "体育", "财经", "科技",
            "汽车", "搞笑", "人民日报", "央视财经", "北京"};
    private String[] titles1 = new String[]{"精选", "奇趣", "美女", "故事"};
    private ArrayAdapter<String> adapter;
    private String cate_id = "1";
    private List<Bitmap> list;
    private GridAdapter gv_adapter;

    private AppContext appContext;
    private UserInfo userInfo;
    private String title1;
    private String content;
    private RequestQueue requestQueue;
    private boolean islogin;
    private String token;

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private ProgressDialog dialog;
    private int currentFragment;
    private String addNews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadnews);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        img_uploadnews_back = (ImageView) findViewById(R.id.img_uploadnews_back);
        btn_uploadnews_complete = (Button) findViewById(R.id.btn_uploadnews_complete);
        sp_uploadnews_cate = (Spinner) findViewById(R.id.sp_uploadnews_cate);
        et_uploadnews_title = (EditText) findViewById(R.id.et_uploadnews_title);
        et_uploadnews_content = (EditText) findViewById(R.id.et_uploadnews_content);
        gv_uploadnews_img = (GridView) findViewById(R.id.gv_uploadnews_img);

    }

    private void initData() {
        appContext = AppContext.getInstance();
        userInfo = appContext.getUserInfo();
        requestQueue = appContext.getRequestQueue();
        islogin = appContext.isLogin();
        list = new ArrayList<Bitmap>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        list.add(bitmap);
        gv_adapter = new GridAdapter(this, list);
        gv_uploadnews_img.setAdapter(gv_adapter);
        //list.add
        Bundle bundle = getIntent().getExtras();
        currentFragment = bundle.getInt("current");
        switch (currentFragment) {
            case 0:
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, titles);
                addNews = Constants.ADD_NEWS;
                break;
            case 1:
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, titles1);
                addNews = Constants.ADD_IMG_NEWS;
                break;
        }
        sp_uploadnews_cate.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void addListener() {
        img_uploadnews_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sp_uploadnews_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (currentFragment) {
                    case 0:
                        cate_id = (position + 1) + "";
                        break;
                    case 1:
                        cate_id = (position + 11) + "";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_uploadnews_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islogin) {
                    title1 = et_uploadnews_title.getText().toString();
                    content = et_uploadnews_content.getText().toString();
                    if (!title1.equals("") && !content.equals("")) {
                        dialog = new ProgressDialog(UploadNewsAcitivity.this);
                        dialog.setMessage("新闻提交中。。。");
                        submitNews();
                    } else {
                        Toast.makeText(appContext, "标题或内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UploadNewsAcitivity.this, "登陆后才能发表", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(UploadNewsAcitivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 3000);
                }
            }
        });
        gv_uploadnews_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (list.size() - 1)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewsAcitivity.this);
                    String[] item = new String[]{"从相册中获取", "拍照"};
                    builder.setItems(item, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
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
            }
        });
    }

    private void submitNews() {
        StringBuilder sb = new StringBuilder();

        sb.append(addNews);
        sb.append("?token=").append(token);
        sb.append("&title=").append(title1);
        sb.append("&content=").append(content);
        sb.append("&cate_id=").append(cate_id);
        String url = sb.toString();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextNewsDetail newsDetail = JsonUtils.parseTextNewsDetail(response);
                if (newsDetail != null) {
                    finish();
                    Toast.makeText(appContext, "发表成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(appContext, JsonUtils.parseMessage(response), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(appContext, "网络错误", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = appContext.getUserInfo();
        islogin = appContext.isLogin();
        if (islogin) {
            token = userInfo.getToken();
        }
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
                Toast.makeText(UploadNewsAcitivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                list.add(list.size() - 1, bitmap);
                gv_adapter.notifyDataSetChanged();
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