package com.example.shengxi.eie.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.base.BaseHandler;
import com.example.shengxi.eie.beans.Forum;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ShengXi on 2017/5/11.
 */

public class ForumPostActivity extends AppCompatActivity {

    private static final String SUCCESS = "SUCCESS";
    @BindView(R.id.post_title)
    EditText postTitle;

    @BindView(R.id.post_summary)
    EditText postSummary;

    @BindView(R.id.post_back)
    ImageView fourmPostBack;

    @BindView(R.id.post_main)
    EditText postMain;

    @BindView(R.id.post_ok)
    TextView textView;

    private String title;
    private String summary;
    private String main;
    private NetUtils utils;

    Handler handler = new BaseHandler<>(new BaseHandler.BaseHandlerCallBack() {

        @Override
        public void callBack(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(ForumPostActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 2) {
                Toast.makeText(ForumPostActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        utils = new NetUtils();
        if (postMain.getText().length()>=0&&postTitle.getText().length()>=0){
            textView.setVisibility(View.VISIBLE);
        }
        fourmPostBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main = postMain.getText().toString();
                summary = postSummary.getText().toString();
                title = postTitle.getText().toString();
                if (("".equals(main) || "".equals(summary) || "".equals(title))) {
                    Toast.makeText(ForumPostActivity.this, "有一行为空！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("不为空", "不为空");
                    if (utils.netState(ForumPostActivity.this)) {
                        gotoThread();
                    }
                }
            }
        });
    }

    private void gotoThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = utils.loginByPost(getDate(), DataUtils.ForumUpUrl);
                Log.w("data", getDate());
                if (!"".equals(result)) {
                    if (SUCCESS.equals(result)) {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }).start();
    }

    private String getDate() {

        Gson gson = new Gson();
        List<Forum> forums = new ArrayList<>();
        Forum fo = new Forum();
        fo.setArticleMain(main);
        fo.setArticleUpId(getUserId());
        fo.setArticleTitle(title);
        fo.setArticleSummary(summary);
        forums.add(fo);
        return "{\"forum\":" + gson.toJson(forums) + " }";
    }

    private String getUserId() {

        String sid = null;
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("select *from student", null);
        while (cur.moveToNext()) {
            sid = cur.getString(cur.getColumnIndex("id"));
        }
        cur.close();
        db.close();
        return sid;
    }
}
