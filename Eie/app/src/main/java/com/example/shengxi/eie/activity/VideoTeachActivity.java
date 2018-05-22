package com.example.shengxi.eie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.adapter.ForumInfoAdapter;
import com.example.shengxi.eie.base.BaseHandler;
import com.example.shengxi.eie.beans.Comments;
import com.example.shengxi.eie.beans.CommentsBean;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.ThumbnailUtils;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.provider.MediaStore;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 *
 * Created by ShengXi on 2017/4/20.
 */

public class VideoTeachActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SUCCESS = "SUCCESS";
    //private static final String PATH = DataUtils.baseUrl + "/AppSwevlet/video/003.avi";
    private String path;
    private FrameLayout mFlVideoGroup;

    private VideoView videoView;
    private MediaController mediaController;
    /*需要隐藏显示的View*/
    private ArrayList<View> mViews;
    private TextView mName;
    private TextView mTtile;
    private TextView mSummary;
    private TextView percentTv;
    private ImageView mIvThumbnail;
    private ImageView mIvStart;
    //评论
    private EditText editText;
    private ListView listView;
    private String edText;
    private NetUtils utils;
    private CommentsBean commentsBean;

    private String[] dataStringArr;

    private AppBarLayout mappBar;
    private ButtonBarLayout mBtnLa;
    Handler handler = new BaseHandler<>(new BaseHandler.BaseHandlerCallBack() {
        @Override
        public void callBack(Message msg) {
            if (msg.what == 111) {
                Toast.makeText(VideoTeachActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                gotoThreadGetData();
            } else if (msg.what == 1) {
                ForumInfoAdapter adapter = new ForumInfoAdapter(VideoTeachActivity.this, commentsBean);
                listView.setAdapter(adapter);
                setListViewHeight(listView);
            }
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideStatusBar(getWindow(),true);
//        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)){
//            return;
//        }

        setContentView(R.layout.activity_video_teach);

        mappBar = (AppBarLayout) findViewById(R.id.app_bar);
        Toolbar mtoolBar = (Toolbar) findViewById(R.id.toolbar);

        mBtnLa = (ButtonBarLayout) findViewById(R.id.playButton);
        mtoolBar.setTouchscreenBlocksFocus(true);
        mappBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRangle = appBarLayout.getTotalScrollRange();
                if (verticalOffset == 0) {
                    mBtnLa.setAlpha(0.0f);
                    mIvStart.setAlpha(1.0f);
                } else {
                    float alpha = Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10;
                    mBtnLa.setAlpha(alpha);
                    mIvStart.setAlpha(-alpha);
                }

            }
        });

        Intent in = getIntent();
        dataStringArr = in.getStringArrayExtra("data");
        path = DataUtils.baseUrl + dataStringArr[3];
        init();

        if (utils.netState(this)) {
            gotoThreadGetData();
        }

        setData();
    }

    private void gotoThreadGetData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = utils.getRequestForum(dataStringArr[4], DataUtils.CommentsUrl);
                if (result != null) {
                    commentsBean = paraseGson(result);
                    handler.sendEmptyMessage(1);
                    Log.e("comments", result);
                }
            }
        }).start();
    }

    private CommentsBean paraseGson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, CommentsBean.class);
    }

    private void setData() {
        mTtile.setText(dataStringArr[1]);
        mName.setText(dataStringArr[0]);
        mSummary.setText(dataStringArr[2]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置缩略图,Vitamio提供的工具类。
                final Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(
                        VideoTeachActivity.this, path
                        , MediaStore.Video.Thumbnails.MINI_KIND);
                if (videoThumbnail != null) {
                    mIvThumbnail.post(new Runnable() {
                        @Override
                        public void run() {
                            mIvThumbnail.setImageBitmap(videoThumbnail);
                        }
                    });
                }

            }
        }).start();
    }

    private void init() {
        //Vitamio.initialize(this);
        utils = new NetUtils();
        percentTv = (TextView) findViewById(R.id.precent);
        mName = (TextView) findViewById(R.id.video_name);
        mSummary = (TextView) findViewById(R.id.video_summary);
        mTtile = (TextView) findViewById(R.id.video_title);
        videoView = (VideoView) findViewById(R.id.videoView);
        mIvStart = (ImageView) findViewById(R.id.iv_video_start);
        mIvThumbnail = (ImageView) findViewById(R.id.iv_video_thumbnail);

        mFlVideoGroup = (FrameLayout) findViewById(R.id.videogroup);
        mediaController = new MediaController(this,true,mFlVideoGroup);
        mediaController.hide();
        mediaController.setVisibility(View.GONE);
        // 评论
        editText = (EditText) findViewById(R.id.video_reText);
        Button buttonOk = (Button) findViewById(R.id.video_btn_ok);
        listView = (ListView) findViewById(R.id.video_comments);

        mIvStart.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        mBtnLa.setOnClickListener(this);



    }

    private void gotoThreadUpData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = utils.getRequest(setDataToJson());

                if (result != null) {

                    if (SUCCESS.equals(result)) {
                        handler.sendEmptyMessage(111);
                    } else {
                        handler.sendEmptyMessage(222);
                    }
                }

            }
        }).start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        boolean mIsFullScreen;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mIsFullScreen = true;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            hideViews(true);
            //重新设置布局
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mFlVideoGroup.setLayoutParams(params);
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        } else {
            mIsFullScreen = false;

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            hideViews(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT,
                    dip2px(this, 220));
            mFlVideoGroup.setLayoutParams(params);

        }
    }


    public void hideViews(boolean hide) {
        if (hide) {
            for (int i = 0; i < mViews.size(); i++) {
                mViews.get(i).setVisibility(View.GONE);
            }
        } else {
            for (int i = 0; i < mViews.size(); i++) {
                mViews.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    public static int dip2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_video_start:
                startVideo();
                mappBar.setTouchscreenBlocksFocus(false);
                break;
            case R.id.video_btn_ok:
                sendComments();
                break;
            case R.id.playButton:
                startVideo();
                mappBar.setExpanded(true);
                mappBar.setTouchscreenBlocksFocus(false);
                break;
        }

    }

    /**
     * 发送评论
     */
    private void sendComments() {

        if ("".equals(editText.getText().toString())) {
            Toast.makeText(VideoTeachActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
        } else {
            edText = editText.getText().toString();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            if (!isEmpty()) {
                Toast.makeText(VideoTeachActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            } else {
                gotoThreadUpData();
            }
            editText.setText("");
        }
    }

    /**
     * 开始播放
     */
    private void startVideo() {
        mediaController.hide();
        mIvStart.setVisibility(View.INVISIBLE);
        mIvThumbnail.setVisibility(View.GONE);
        videoView.setVideoPath(path);
        videoView.setMediaController(mediaController);
        //videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //videoView.start();
                mp.start();
                videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
                mp.setPlaybackSpeed(1.0f);

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                //停止播放
                mp.stop();
                mIvStart.setVisibility(View.VISIBLE);
                mappBar.setTouchscreenBlocksFocus(true);
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        percentTv.setVisibility(View.VISIBLE);
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        percentTv.setVisibility(View.GONE);
                        mp.start();
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //显示网速
                        break;
                }
                return true;
            }
        });

        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                percentTv.setText("已缓冲：" + percent + "%");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            //清除缓存
            videoView.destroyDrawingCache();
            //停止播放
            videoView.stopPlayback();
            videoView = null;
        }
    }

    public void hideStatusBar(Window window, boolean enable) {

        WindowManager.LayoutParams p = window.getAttributes();
        if (enable)
        //|=：或等于，取其一
        {
            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else
        //&=：与等于，取其二同时满足，     ~ ： 取反
        {
            p.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        window.setAttributes(p);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 判断用户是否登陆
     *
     * @return ture or false
     */
    public boolean isEmpty() {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from student", null);
        int index = 0;
        while (cur.moveToNext()) {
            index++;
        }
        cur.close();
        db.close();
        return (index == 0);
    }

    public String setDataToJson() {

        Gson gson = new Gson();
        List<Comments> commentses = new ArrayList<>();
        Comments comment = new Comments();
        comment.setCommentsId(dataStringArr[4]);
        comment.setMainText(edText);
        comment.setCommentsDate(getNowDate());
        comment.setCommentsSid(getUserId());
        commentses.add(comment);
        return "{\"comments\":" + gson.toJson(commentses) + " }";
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

    public String getNowDate() {
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        String mDate = simpleDateFormat.format(new Date());
        // Log.w("now date",date);
        return mDate;
    }

    /**
     * 重写listview高度解决华东冲突
     *
     * @param listView lisuvie
     */
    private void setListViewHeight(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}