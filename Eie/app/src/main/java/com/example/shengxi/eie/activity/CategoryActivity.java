package com.example.shengxi.eie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.adapter.CategoryAdapter;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by ShengXi on 2017/5/8.
 */

public class CategoryActivity extends AppCompatActivity{

    private PullToRefreshListView listview;
    private NetUtils utils;
    private String cateString;
    private ImageView cateBack;
    private ClassMenuBean dataBean;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                CategoryAdapter cateAdapter = new CategoryAdapter(CategoryActivity.this,dataBean);
                listview.setAdapter(cateAdapter);
               // Log.e("categoryData",dataBean.classes.get(0).summary);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent input = getIntent();
        cateString = input.getStringExtra("data");
        initView();
        if (utils.netState(this)){
            gotoThread();
        }else{
            Toast.makeText(this,"无网络",Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String result = utils.getRequestForum(cateString, DataUtils.CategoryClassUrl);
                if (result!=null){
                    Log.w("category",result);
                    dataBean = paraseGson(result);
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(2);
                }

            }
        }).start();
    }


    private void initView() {
        utils = new NetUtils();
        listview = (PullToRefreshListView) findViewById(R.id.category_list);
        cateBack = (ImageView) findViewById(R.id.cate_back);
        cateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String []arr = {dataBean.classes.get(i-1).teacher,dataBean.classes.get(i-1).title,dataBean.classes.get(i-1).summary,dataBean.classes.get(i-1).url,dataBean.classes.get(i-1).id};
                Intent in = new Intent(CategoryActivity.this, VideoTeachActivity.class);
                in.putExtra("data",arr);
                startActivity(in);
            }
        });
    }

    private ClassMenuBean paraseGson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result,ClassMenuBean.class);
    }

}
