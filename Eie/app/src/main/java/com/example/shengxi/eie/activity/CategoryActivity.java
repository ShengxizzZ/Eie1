package com.example.shengxi.eie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.adapter.CategoryAdapter;
import com.example.shengxi.eie.base.BaseHandler;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * 推送界面
 * Created by ShengXi on 2017/5/8.
 */

public class CategoryActivity extends AppCompatActivity{

    @BindView(R.id.cate_back)
    ImageView mcateBack;

    @BindView(R.id.category_list)
    RecyclerView listview;

    private NetUtils utils;
    private String cateString;
    private ClassMenuBean dataBean;
    DefaultLoadingLayout dm ;

    //使用weakreference封装handler解决handler内存泄漏问题
    private Handler handler = new BaseHandler<>(new BaseHandler.BaseHandlerCallBack() {
        @Override
        public void callBack(Message msg) {
            if (msg.what == 1){
                RecyclerView.LayoutManager lm = new LinearLayoutManager(CategoryActivity.this, LinearLayoutManager.HORIZONTAL,false);
                CategoryAdapter categoryAdapter = new CategoryAdapter(CategoryActivity.this,dataBean);
                listview.setLayoutManager(lm);
                listview.setAdapter(categoryAdapter);
                dm.onDone();
            }

        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        Intent input = getIntent();
        cateString = input.getStringExtra("data");
        initView();
        dm.onLoading();
        if (NetUtils.netState(this)){

            gotoThread();
        }else{
            dm.onError();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        dm.clear();
    }

    private void initView() {

        dm = SmartLoadingLayout.createDefaultLayout(this,listview);
        utils = new NetUtils();
        mcateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private ClassMenuBean paraseGson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result,ClassMenuBean.class);
    }
}
