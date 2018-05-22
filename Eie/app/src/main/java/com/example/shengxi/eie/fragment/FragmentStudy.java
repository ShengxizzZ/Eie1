package com.example.shengxi.eie.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.SearchActivity;

import com.example.shengxi.eie.adapter.ClassMenuAdapter;

import com.example.shengxi.eie.adapter.ViewPagerAdapter;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.network.ApiService;
import com.example.shengxi.eie.network.manager.RetrofitNetManager;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 *
 * Created by ShengXi on 2017/4/12.
 */

public class FragmentStudy extends android.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.viewPager)
    RollPagerView viewPager;

    @BindView(R.id.swip_refresh_layout)
    SwipeRefreshLayout swiprRe;

    @BindView(R.id.stu_main_appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.stu_main_search_btn)
    ButtonBarLayout mBtnBar;

    @BindView(R.id.list_class)
    RecyclerView lv;

    private NetUtils utils;
    static ClassMenuBean classMenuData;


    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        utils = new NetUtils();
        initView();
        swiprRe.setRefreshing(true);
        if (NetUtils.netState(getActivity())) {
            gotoThread();
            //gotoHotThread();
        }
        return view;
    }

    private void gotoThread() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain,utf-8"),DataUtils.CATEONE);
        ApiService service = RetrofitNetManager.getInstance().getForumService(getActivity());
        service.getHotClassMenu(requestBody)
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, ClassMenuBean>() {
                    @Override
                    public ClassMenuBean call(ResponseBody responseBody) {
                        Log.w("reHot",responseBody.byteStream().toString());
                        return  new Gson().fromJson(NetUtils.getResponseBody(responseBody), ClassMenuBean.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ClassMenuBean>() {
                    @Override
                    public void onCompleted() {
                        swiprRe.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swiprRe.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ClassMenuBean hotClassMenuBean) {
                        //hotClassBean =hotClassMenuBean;
                        Log.w("hot",hotClassMenuBean.classes.toString());
                        ViewPagerAdapter adapter = new ViewPagerAdapter(hotClassMenuBean);
                        viewPager.setAdapter(adapter);

                    }
                });
//
//        Observable.zip(
//                service.getClassMenu(),
//                service.getHotClassMenu(requestBody),
//                new Func2<ResponseBody, ResponseBody, Object>() {
//                }
//        );
        Observable<ResponseBody> observable = RetrofitNetManager
                .getInstance()
                .getForumService(getActivity())
                .getClassMenu();
        observable
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, ClassMenuBean>() {
                    @Override
                    public ClassMenuBean call(ResponseBody responseBody) {
                        Log.e("ClassBean", responseBody.byteStream().toString());
                        return new Gson().fromJson(NetUtils.getResponseBody(responseBody), ClassMenuBean.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ClassMenuBean>() {
                    @Override
                    public void onCompleted() {
                        swiprRe.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ClassMenuBean classMenuBean) {
                        //setDataToView();
                        classMenuData = classMenuBean;
                        ClassMenuAdapter adpter = new ClassMenuAdapter(getActivity(), classMenuBean);
                        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
                        lv.setLayoutManager(lm);
                        lv.setAdapter(adpter);
                    }
                });
    }


    private void initView() {
        viewPager.setPlayDelay(3000);
        viewPager.setAnimationDurtion(500);
        viewPager.setHintView(new ColorPointHintView(getActivity(), Color.GRAY,Color.WHITE));

        mBtnBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });
        swiprRe.setOnRefreshListener(this);
        swiprRe.setColorSchemeColors(getResources().getColor(R.color.black_80),
                getResources().getColor(R.color.gray_20),
                getResources().getColor(R.color.black_80),
                getResources().getColor(R.color.colorAccent));

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swiprRe.setEnabled(true);
                } else {
                    swiprRe.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (NetUtils.netState(getActivity())) {
            gotoThread();
        }
    }
}
//    //实现图片轮播
//    private void initData() {
//        mImageViewList = new ArrayList<>();
//        mImageViewDotList = new ArrayList();
//        ImageView imageView;
//        for (int i = 0; i < hotClassBean.classes.size() + 2; i++) {
//            if (i == 0) {   //判断当i=0为该处的ImageView设置最后一张图片作为背景
//                imageView = new ImageView(getActivity());
//                //imageView.setBackgroundResource(images[images.length-1]);
//                getUrlImage(i,imageView);
//            } else if (i == hotClassBean.classes.size() + 1) {   //判断当i=images.length+1时为该处的ImageView设置第一张图片作为背景
//                imageView = new ImageView(getActivity());
//                //imageView.setBackgroundResource(images[0]);
//                getUrlImage( 0,imageView);
//
//            } else {  //其他情况则为ImageView设置images[i-1]的图片作为背景
//                imageView = new ImageView(getActivity());
//                getUrlImage(i-1,imageView);
//            }
//        }
//    }
//
//    private void getUrlImage(int i, ImageView imageView) {
//        Picasso.with(getActivity())
//                .load(DataUtils.baseUrl+ hotClassBean.classes.get(i).img)
//                .placeholder(R.mipmap.ic_22)
//                .into(imageView);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        mImageViewList.add(imageView);
//    }
//
//    //  设置轮播小圆点
//    private void setDot() {
//        //  设置LinearLayout的子控件的宽高，这里单位是像素。
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(45, 45);
//        params.rightMargin = 20;
//        //  for循环创建images.length个ImageView（小圆点）
//        for (int i = 0; i < hotClassBean.classes.size(); i++) {
//            ImageView imageViewDot = new ImageView(getActivity());
//            imageViewDot.setLayoutParams(params);
//
//            imageViewDot.setBackgroundResource(R.mipmap.ic_indicator_not_select);
//            llIndicator.addView(imageViewDot);
//            mImageViewDotList.add(imageViewDot);
//        }
//        mImageViewDotList.get(dotPosition).setBackgroundResource(R.mipmap.ic_indicator_select);
//    }
//
//    private void setViewPager() {
//        MyPagerAdapter adapter = new MyPagerAdapter(mImageViewList,hotClassBean,getActivity());
//        vp.setAdapter(adapter);
//        vp.setCurrentItem(currentPosition);
//        //页面改变监听
//        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
//                    currentPosition = hotClassBean.classes.size();
//                    dotPosition = hotClassBean.classes.size() - 1;
//                } else if (position == hotClassBean.classes.size() + 1) {    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
//                    currentPosition = 1;
//                    dotPosition = 0;
//                } else {
//                    currentPosition = position;
//                    dotPosition = position - 1;
//                }
//                //  把之前的小圆点设置背景为暗红，当前小圆点设置为红色
//                mImageViewDotList.get(prePosition).setBackgroundResource(R.mipmap.ic_indicator_not_select);
//                mImageViewDotList.get(dotPosition).setBackgroundResource(R.mipmap.ic_indicator_select);
//                prePosition = dotPosition;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                //当state为SCROLL_STATE_IDLE即没有滑动的状态时切换页面
//                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    vp.setCurrentItem(currentPosition, false);
//                }
//            }
//        });
//
//    }
//
//    //  设置自动播放
//    private void autoPlay() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                while (true) {
//                    SystemClock.sleep(3000);
//                    currentPosition++;
//                    handler.sendEmptyMessage(999);
//                }
//            }
//        }.start();
//    }