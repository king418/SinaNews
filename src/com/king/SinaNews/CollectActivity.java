package com.king.SinaNews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.king.utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AUTHOR: King
 * DATE: 2015/4/27.
 */
public class CollectActivity extends Activity {

    private ListView list_collect;
    private ImageView img_collect_back;
    private MySQLiteOpenHelper dbHelper;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> list;
    private ImageView img_collect_nothing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
        initData();
        addListener();
    }

    private void initView(){
        img_collect_back = (ImageView) findViewById(R.id.img_collect_back);
        list_collect = (ListView) findViewById(R.id.list_collect);
        img_collect_nothing = (ImageView) findViewById(R.id.img_collect_nothing);
    }
    private void initData(){
        dbHelper = new MySQLiteOpenHelper(this);
        list = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(this,list,R.layout.item_collect_list,
                new String[]{"title","content"},
                new int[]{R.id.item_collect_title,R.id.item_collect_content});
        list_collect.setEmptyView(img_collect_nothing);
        list_collect.setAdapter(adapter);
    }

    private void reloadList(){
        list.clear();
        List<Map<String, Object>> maps = dbHelper.selectList("select * from tb_collect", null);
        list.addAll(maps);
        adapter.notifyDataSetChanged();
    }

    private void addListener(){
        img_collect_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectActivity.this, TextNewsContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id", list.get(position).get("news_id").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }
}