package com.example.shengxi.eie.fragment;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.VideoTeachActivity;
import com.example.shengxi.eie.adapter.ClassMenuAdapter;
import com.example.shengxi.eie.adapter.MyPagerAdapter;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.MyGridView;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static android.R.id.list;

/**
 * Created by ShengXi on 2017/4/12.
 */

public class FragmentStudy extends android.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swiprRe;
    private ViewPager vp;
    private LinearLayout llIndicator;
    private MyGridView lv;
    private NetUtils utils;
    static ClassMenuBean classMenuData;
    //图片轮播
    private List<ImageView> mImageViewList;
    private int currentPosition = 1;
    private int dotPosition = 0;
    private int prePosition = 0;
    private List<ImageView> mImageViewDotList;
    private RequestQueue queue;
    private ClassMenuBean hotClassBean;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                Toast.makeText(getActivity(), "获取数据成功", Toast.LENGTH_SHORT).show();

                initData();
                setDot();
                setViewPager();
                autoPlay();
            } else if (msg.what == 2) {
                Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                setDataToView();
            } else if (msg.what == 111) {
                setDataToView();
            } else if (msg.what == 999) {
                vp.setCurrentItem(currentPosition, false);
            }
            swiprRe.setRefreshing(false);
        }
    };


    @Override
    public void onResume() {
        super.onResume();


        //gotoThread();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_main, container, false);

        queue = Volley.newRequestQueue(getActivity());

        initView(view);

        utils = new NetUtils();
        swiprRe.setRefreshing(true);
        if (utils.netState(getActivity())) {
            gotoThread();
            gotoHotThread();
       }

        return view;
    }

    private void setDataToView() {
        ClassMenuAdapter adapter = new ClassMenuAdapter(getActivity(), classMenuData);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClassMenuAdapter.Item item = (ClassMenuAdapter.Item) view.getTag();
                Intent in = new Intent(getActivity(), VideoTeachActivity.class);
                String []arr = {classMenuData.classes.get(i).teacher,classMenuData.classes.get(i).title,classMenuData.classes.get(i).summary,classMenuData.classes.get(i).url,classMenuData.classes.get(i).id};

                in.putExtra("data",arr);
                getActivity().startActivity(in);

               Log.e("sss",item.index.getText().toString());
            }
        });
    }

    private void gotoThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = utils.getData(DataUtils.classMenuServlet);
                if (result == null) {
                    handler.sendEmptyMessage(2);
                } else {

                    classMenuData = utils.paserGson(result.trim());
                    handler.sendEmptyMessage(111);
                }
            }
        }).start();
    }

    private void initView(View view) {
        swiprRe = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_layout);
        swiprRe.setOnRefreshListener(this);
        swiprRe.setColorSchemeColors(getResources().getColor(R.color.black_80), getResources().getColor(R.color.gray_20), getResources().getColor(R.color.black_80), getResources().getColor(R.color.colorAccent));
        vp = (ViewPager) view.findViewById(R.id.view_home);
        llIndicator = (LinearLayout) view.findViewById(R.id.indicator);
        lv = (MyGridView) view.findViewById(R.id.list_class);

    }

    @Override
    public void onRefresh() {

        if (utils.netState(getActivity())) {
            gotoThread();
            //gotoHotThread();
        }
        //handler.sendEmptyMessageDelayed(0, 2000);

    }

    //实现图片轮播
    private void initData() {
        mImageViewList = new ArrayList<>();
        mImageViewDotList = new ArrayList();
        ImageView imageView;
        for (int i = 0; i < hotClassBean.classes.size() + 2; i++) {
            if (i == 0) {   //判断当i=0为该处的ImageView设置最后一张图片作为背景
                imageView = new ImageView(getActivity());
                //imageView.setBackgroundResource(images[images.length-1]);
                getUrlImage(i,imageView);
            } else if (i == hotClassBean.classes.size() + 1) {   //判断当i=images.length+1时为该处的ImageView设置第一张图片作为背景
                imageView = new ImageView(getActivity());
                //imageView.setBackgroundResource(images[0]);
                getUrlImage( 0,imageView);

            } else {  //其他情况则为ImageView设置images[i-1]的图片作为背景
                imageView = new ImageView(getActivity());
                getUrlImage(i-1,imageView);
            }
        }
    }

    private void getUrlImage(int i, ImageView imageView) {


        //imageView.setBackgroundResource(images[i-1]);
        final ImageView finalImageView = imageView;
        ImageRequest request = new ImageRequest(DataUtils.baseUrl
                + hotClassBean.classes.get(i).img, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                // TODO Auto-generated method stub
                finalImageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });
        queue.add(request);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageViewList.add(imageView);
    }

    //  设置轮播小圆点
    private void setDot() {
        //  设置LinearLayout的子控件的宽高，这里单位是像素。
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(45, 45);
        params.rightMargin = 20;
        //  for循环创建images.length个ImageView（小圆点）
        for (int i = 0; i < hotClassBean.classes.size(); i++) {
            ImageView imageViewDot = new ImageView(getActivity());
            imageViewDot.setLayoutParams(params);

            imageViewDot.setBackgroundResource(R.mipmap.ic_indicator_not_select);
            llIndicator.addView(imageViewDot);
            mImageViewDotList.add(imageViewDot);
        }
        mImageViewDotList.get(dotPosition).setBackgroundResource(R.mipmap.ic_indicator_select);
    }

    private void setViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(mImageViewList,hotClassBean,getActivity());
        vp.setAdapter(adapter);
        vp.setCurrentItem(currentPosition);
        //页面改变监听
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
                    currentPosition = hotClassBean.classes.size();
                    dotPosition = hotClassBean.classes.size() - 1;
                } else if (position == hotClassBean.classes.size() + 1) {    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                    currentPosition = 1;
                    dotPosition = 0;
                } else {
                    currentPosition = position;
                    dotPosition = position - 1;
                }
                //  把之前的小圆点设置背景为暗红，当前小圆点设置为红色
                mImageViewDotList.get(prePosition).setBackgroundResource(R.mipmap.ic_indicator_not_select);
                mImageViewDotList.get(dotPosition).setBackgroundResource(R.mipmap.ic_indicator_select);
                prePosition = dotPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //当state为SCROLL_STATE_IDLE即没有滑动的状态时切换页面
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    vp.setCurrentItem(currentPosition, false);
                }
            }
        });

    }

    //  设置自动播放
    private void autoPlay() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    SystemClock.sleep(3000);
                    currentPosition++;
                    handler.sendEmptyMessage(999);
                }
            }
        }.start();
    }

    private void gotoHotThread() {

        final Gson gson = new Gson();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String result = utils.getRequestForum(DataUtils.CATEONE, DataUtils.CategoryClassUrl);
                if (result!=null){
                    Log.w("category",result);
                    hotClassBean = gson.fromJson(result,ClassMenuBean.class);
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(2);
                }

            }
        }).start();
    }
}
