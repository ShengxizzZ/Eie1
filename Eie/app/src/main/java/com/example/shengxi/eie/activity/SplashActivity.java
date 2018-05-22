package com.example.shengxi.eie.activity;

import android.content.Intent;
import android.database.Observable;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.base.BaseHandler;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * 广告界面
 * Created by ShengXi on 2017/4/17.
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    //private RequestQueue queue
    Handler handler  = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideStatusBar(getWindow(), true);
        imageView = (ImageView) findViewById(R.id.splash_imageview);



        /**
         * 进阶优化使用在mainactivity的fregment实现splashactivity
         *
         */
        // DataBaseHelper db = new DataBaseHelper(this);


        // queue = Volley.newRequestQueue(this);

//        Picasso.Builder picassoBuilder = new Picasso.Builder(this);
//        picassoBuilder.downloader(new OkHttpDownloader(new OkHttpClient()));
//        Picasso picasso = picassoBuilder.build();
//        picasso.load(DataUtils.baseUrl + "/AppSwevlet/img/g01.jpg")
//                .placeholder(R.mipmap.ic_bili_logo_2016)
//                .into(imageView);

//        ImageRequest request = new ImageRequest(DataUtils.baseUrl+"/AppSwevlet/img/g01.jpg", new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//
//            }
//        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        queue.add(request);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

    }

}