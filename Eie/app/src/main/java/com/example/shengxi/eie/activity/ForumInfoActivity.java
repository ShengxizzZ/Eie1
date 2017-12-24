package com.example.shengxi.eie.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.adapter.ForumInfoAdapter;
import com.example.shengxi.eie.beans.Comments;
import com.example.shengxi.eie.beans.CommentsBean;
import com.example.shengxi.eie.beans.ForumBean;
import com.example.shengxi.eie.beans.ForumInfoBean;
import com.example.shengxi.eie.fragment.FragmentMsg;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ShengXi on 2017/4/24.
 */

public class ForumInfoActivity extends AppCompatActivity implements FragmentMsg.CallBack{

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILS = "FAILS";
    private RequestQueue queue;
    private ListView listView;
    private CircleImageView imageView;
    private TextView upName;
    private TextView upDate;
    private TextView mainText;
    private ImageView forumBack;
    private Button btnOk;
    private EditText text;
    private ForumInfoBean dataBean;
    private String[] temp;
    private String stringText;
    private NetUtils utils;
    private CommentsBean commentsBean;
    private ForumInfoAdapter adapter;

    private ScrollView scrollView;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){

                adapter = new ForumInfoAdapter(ForumInfoActivity.this,commentsBean);
                listView.setAdapter(adapter);
                setListViewHeight(listView);

            }else if(msg.what == 111){
                Toast.makeText(ForumInfoActivity.this,"评论成功！",Toast.LENGTH_SHORT).show();
                gotoThreadGetData();
            }else if(msg.what == 222){

            }
        }
    };

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null){
            return;
        }
        int totalHeight = 0;
        for (int i = 0;i<listAdapter.getCount();i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight+=listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+(listView.getDividerHeight()*(listAdapter.getCount()-1));
        listView.setLayoutParams(params);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_info);
        Intent in = getIntent();
        temp = in.getStringArrayExtra("data");
        //listView.setAdapter(adapter);
        initView();
        setData();

        if (utils.netState(this)){
            gotoThreadGetData();
        }

    }

    private void gotoThreadGetData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = utils.getRequestForum(temp[4],DataUtils.CommentsUrl);
                if (result!=null){
                    commentsBean = paraseGson(result);
                    handler.sendEmptyMessage(1);
                    Log.e("comments",result);
                }


            }
        }).start();
    }

    private CommentsBean paraseGson(String result) {

        Gson gson = new Gson();
        return gson.fromJson(result,CommentsBean.class);
    }


    private void setData() {

        ImageRequest request = new ImageRequest(DataUtils.baseUrl + temp[1], new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
        upName.setText(temp[0]);
        upDate.setText(temp[3]);
        mainText.setText(temp[2]);
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        queue = Volley.newRequestQueue(this);
        utils = new NetUtils();
        listView = (ListView) findViewById(R.id.foruminfo_refrelist);
       // listView.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        imageView = (CircleImageView) findViewById(R.id.foruminfo_head_circleview);
        upName = (TextView) findViewById(R.id.foruminfo_head_name);
        upDate = (TextView) findViewById(R.id.foruminfo_head_date);
        mainText = (TextView) findViewById(R.id.foruminfo_head_main);
        btnOk = (Button) findViewById(R.id.forumInfo_btn_ok);
        text = (EditText) findViewById(R.id.forumInfo_reText);
        forumBack = (ImageView) findViewById(R.id.foruminfo_back);
        forumBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(text.getText().toString())){
                    Toast.makeText(ForumInfoActivity.this,"评论不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(text.getWindowToken(),0);
                    stringText = text.getText().toString();
                    if (!isEmpty()){
                        Toast.makeText(ForumInfoActivity.this,"请先登录！",Toast.LENGTH_SHORT).show();
                    }else{

                        gotoThreadUpData();

                    }
                    text.setText("");
                }



            }
        });
    }

    private void gotoThreadUpData() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                String result = utils.getRequest(setDataToJson());

                if(result!=null){

                    if(SUCCESS.equals(result)){
                        handler.sendEmptyMessage(111);
                    }else{
                        handler.sendEmptyMessage(222);
                    }
                }

            }
        }).start();
    }


    @Override
    public void getData(ForumBean dataBean) {
        if (dataBean!=null){
            Log.w("CallBackData",dataBean.toString());
        }
    }

    public boolean isEmpty(){
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from student",null);
        int index = 0;
        while (cur.moveToNext()){
            index++;
        }

        if (index == 0){
            return false;
        }else{
            return true;
        }

    }
    public String getNowDate(){
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(new Date());
       // Log.w("now date",date);
        return  date;
    }

    public String setDataToJson(){

        Gson gson = new Gson();
        List<Comments> commentses = new ArrayList<>();
        Comments comment = new Comments();
        comment.setCommentsId(temp[4]);
        comment.setMainText(stringText);
        comment.setCommentsDate(getNowDate());
        comment.setCommentsSid(getUserId());
        commentses.add(comment);

        return  "{\"comments\":" + gson.toJson(commentses) + " }";

    }

    private String getUserId() {

        String sid = null;
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("select *from student",null);
        while (cur.moveToNext()){
            sid = cur.getString(cur.getColumnIndex("id"));

        }
        db.close();
        return  sid;
    }

}
